package com.togedocs.backend.domain.repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.togedocs.backend.api.dto.ApidocsRequest;
import com.togedocs.backend.common.exception.BusinessException;
import com.togedocs.backend.common.exception.ErrorCode;
import com.togedocs.backend.domain.entity.Apidocs;
import com.togedocs.backend.domain.entity.ColDto;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ApidocsRepositoryImpl implements ApidocsRepository {

    private final MongoTemplate mongoTemplate;
    private final String APIDOCS = "apidocs";
    private final String PROJECT_ID = "projectId";
    private final String ROWS = "rows";
    private final String COLS = "cols";
    private final String DATA = "data";
    private final int DEFAULT_WIDTH = 100;

    public boolean existsByProjectId(Long projectId) {
        return mongoTemplate.exists(BasicQuery.query(Criteria.where(PROJECT_ID).is(projectId)), APIDOCS);
    }

    @Override
    public void createApidocs(Apidocs apidocs) {
        mongoTemplate.insert(apidocs, APIDOCS);
    }

    @Override
    public void deleteApidocs(Long projectId) {
        Query query = new Query().addCriteria(Criteria.where(PROJECT_ID).is(projectId));
        DeleteResult deleteResult = mongoTemplate.remove(query, APIDOCS);

        if (deleteResult.getDeletedCount() == 0) throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND);
    }

    @Override
    public void addRow(Long projectId) {
        Query query = new Query().addCriteria(Criteria.where(PROJECT_ID).is(projectId));
        Update update = new Update();
        String rowId = UUID.randomUUID().toString();
        update.push(ROWS, rowId);
        update.set(DATA + "." + rowId, new Document());
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, APIDOCS);

        if (updateResult.getMatchedCount() == 0) throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND);
    }


    @Override
    public void addCol(Long projectId, ColDto colDto) {
        Query query = new Query().addCriteria(Criteria.where(PROJECT_ID).is(projectId));
        Update update = new Update();
        update.push(COLS).atPosition(-3).value(colDto);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, APIDOCS);

        if (updateResult.getMatchedCount() == 0) throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND);
    }

    // PROJECT_NOT_FOUND(service) > ROW_NOT_FOUND > UNEXPECTED_ERROR
    @Override
    public void moveRow(Long projectId, ApidocsRequest.MoveItemRequest request) {
        Query query = new Query().addCriteria(Criteria.where(PROJECT_ID).is(projectId));
        Update update = new Update();
        update.pull(ROWS, request.getFromId());
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, APIDOCS);

        // PROJECT_NOT_FOUND catched in service already
        if (updateResult.getModifiedCount() == 0) throw new BusinessException(ErrorCode.ROW_NOT_FOUND);

        update = new Update();
        update.push(ROWS).atPosition(request.getToIndex()).value(request.getFromId());
        updateResult = mongoTemplate.updateFirst(query, update, APIDOCS);

        if (updateResult.getModifiedCount() == 0) throw new BusinessException(ErrorCode.UNEXPECTED_ERROR);
    }

    @Override
    public void moveCol(Long projectId, ApidocsRequest.MoveItemRequest request) {
        Query query = new Query().addCriteria(Criteria.where(PROJECT_ID).is(projectId));
        Apidocs apidocs = mongoTemplate.findOne(query, Apidocs.class);
        Update update = new Update();
        update.pull(COLS, Query.query(Criteria.where("uuid").is(request.getFromId())));
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, APIDOCS);

        if (updateResult.getModifiedCount() == 0) throw new BusinessException(ErrorCode.COL_NOT_FOUND);

        List<ColDto> colDtos = apidocs.getCols();
        ColDto targetCol = new ColDto();
        for (ColDto colDto : colDtos) {
            if (colDto.getUuid().equals(request.getFromId())) {
                targetCol = colDto;
                break;
            }
        }

        if (targetCol == null) throw new BusinessException(ErrorCode.COL_NOT_FOUND);

        update = new Update();
        update.push(COLS).atPosition(request.getToIndex()).value(targetCol);
        updateResult = mongoTemplate.updateFirst(query, update, APIDOCS);

        if (updateResult.getModifiedCount() == 0) throw new BusinessException(ErrorCode.UNEXPECTED_ERROR);
    }

    @Override
    public void deleteRow(Long projectId, String rowId) {
        Query query = new Query().addCriteria(Criteria.where(PROJECT_ID).is(projectId));
        Update update = new Update();
        update.pull(ROWS, rowId);
        update.unset(DATA + "." + rowId);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, APIDOCS);

        if (updateResult.getModifiedCount() == 0) throw new BusinessException(ErrorCode.ROW_NOT_FOUND);
    }

    @Override
    public void deleteCol(Long projectId, String colId) {
        Query query = new Query().addCriteria(Criteria.where(PROJECT_ID).is(projectId));
        query.fields().include(ROWS).include(COLS);
        Apidocs apidocs = mongoTemplate.findOne(query, Apidocs.class, APIDOCS);
        List<String> rows = apidocs.getRows();

        Update update = new Update();
        // #1. cols에서 uuid가 colId인 colDto 제거
        update.pull(COLS, Query.query(Criteria.where("uuid").is(colId)));
        // rows 배열 조회
        for (String rowId : rows) {
            // #2. rows 배열의 rowId를 순회하며 colId쌍 제거
            // TODO: updateMulti를 쓰면 될까??
            update.unset(DATA + "." + rowId + "." + colId);
        }
        // #1, #2 실행
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, APIDOCS);

        if (updateResult.getModifiedCount() == 0) throw new BusinessException(ErrorCode.UNEXPECTED_ERROR);
    }

    @Override
    public boolean updateCell(Long projectId, ApidocsRequest.UpdateCellRequest request) {

        Query query = new Query().addCriteria(Criteria.where(PROJECT_ID).is(projectId));

        Apidocs apidocs = mongoTemplate.findOne(query, Apidocs.class);
        List<String> rowIds = apidocs.getRows();
        boolean exceptionFlag = true;
        for (String rowId : rowIds) {
            if (rowId.equals(request.getRowId())) exceptionFlag = false;
        }
        if (exceptionFlag) return false;
        List<ColDto> colDtos = apidocs.getCols();
        exceptionFlag = true;
        for (ColDto colDto : colDtos) {
            if (colDto.getUuid().equals(request.getColId())) exceptionFlag = false;
        }
        if (exceptionFlag) return false;

        Update update = new Update();
        update.set(DATA + "." + request.getRowId() + "." + request.getColId(), request.getContent());
        mongoTemplate.updateFirst(query, update, APIDOCS);

        return true;
    }

    @Override
    public Apidocs getDocs(Long projectId) {

        Query query = new Query().addCriteria(Criteria.where(PROJECT_ID).is(projectId));
        Apidocs apidocs = mongoTemplate.findOne(query, Apidocs.class, APIDOCS);

        return apidocs;
    }

    @Override
    public boolean updateProjectInfo(Long projectId, ApidocsRequest.UpdateProjectInfoRequest request) {

        Query query = new Query().addCriteria(Criteria.where(PROJECT_ID).is(projectId));
        Update update = new Update();
        update.set("title", request.getTitle());
        update.set("desc", request.getDesc());
        update.set("baseUrl", request.getBaseUrl());
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, APIDOCS);

        if (updateResult.getMatchedCount() == 0) return false;
        return true;
    }

    @Override
    public boolean updateCol(Long projectId, String colId, ApidocsRequest.UpdateColRequest request) {

        Query query = new Query().addCriteria(Criteria.where(PROJECT_ID).is(projectId));
        Update update = new Update();
        update.pull(COLS, Query.query(Criteria.where("uuid").is(colId)));
        Apidocs apidocs = mongoTemplate.findAndModify(query, update, Apidocs.class);

        if (apidocs == null)
            return false; // pjt not found exception, but already checked in service before this method is called

        boolean exceptionFlag = true;
        List<ColDto> colDtos = apidocs.getCols();
        for (ColDto colDto : colDtos) {
            if (colDto.getUuid().equals(colId)) exceptionFlag = false;
        }
        if (exceptionFlag) return false; // col not found exception

        int size = colDtos.size();
        int targetIndex = 0;
        for (int i = 0; i < size; i++) {
            if (colDtos.get(i).getUuid().equals(colId)) {
                targetIndex = i;
            }
        }
        ColDto updatedCol = ColDto.build(colId, request.getName(), request.getType(), request.getWidth(), colDtos.get(targetIndex).getCategory());
        update = new Update();
        update.push(COLS).atPosition(targetIndex).value(updatedCol);
        mongoTemplate.updateFirst(query, update, APIDOCS);

        return true;
    }
}
