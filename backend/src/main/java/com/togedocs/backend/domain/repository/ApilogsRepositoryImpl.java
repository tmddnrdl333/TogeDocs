package com.togedocs.backend.domain.repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.togedocs.backend.common.exception.BusinessException;
import com.togedocs.backend.common.exception.ErrorCode;
import com.togedocs.backend.domain.entity.Apilogs;
import com.togedocs.backend.domain.entity.LogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ApilogsRepositoryImpl implements ApilogsRepository {
    private final MongoTemplate mongoTemplate;
    private final String APILOGS = "apilogs";
    private final String PROJECT_ID = "projectId";

    @Override
    public void createApilogs(Apilogs apilogs) {
        mongoTemplate.insert(apilogs, APILOGS);
    }

    @Override
    public void deleteApilogs(Long projectId) {
        Query query = new Query().addCriteria(Criteria.where(PROJECT_ID).is(projectId));
        DeleteResult deleteResult = mongoTemplate.remove(query, APILOGS);

        if (deleteResult.getDeletedCount() == 0) throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND);
    }

    @Override
    public List<LogDto> getLogs(Long projectId, String rowId) {
        Query query = new Query().addCriteria(Criteria.where(PROJECT_ID).is(projectId));
        query.fields().include("LOG");
        Apilogs apilogs = mongoTemplate.findOne(query, Apilogs.class);

        if (apilogs == null) throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND);

        return apilogs.getLog().get(rowId);
    }

    @Override
    public void addLog(Long projectId, String rowId, LogDto logDto) {
        Query query = new Query().addCriteria(Criteria.where(PROJECT_ID).is(projectId));
        Update update = new Update();
        update.push("log." + rowId, logDto);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, APILOGS);

        if (updateResult.getMatchedCount() == 0) throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND);
        if (updateResult.getModifiedCount() == 0) throw new BusinessException(ErrorCode.UNEXPECTED_ERROR);
    }
}
