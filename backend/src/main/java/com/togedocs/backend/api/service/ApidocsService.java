package com.togedocs.backend.api.service;

import com.togedocs.backend.api.dto.ApidocsRequest;
import com.togedocs.backend.api.dto.ApidocsResponse;
import com.togedocs.backend.api.dto.ProjectRequest;
import com.togedocs.backend.common.exception.BusinessException;
import com.togedocs.backend.common.exception.ErrorCode;
import com.togedocs.backend.domain.entity.Apidocs;
import com.togedocs.backend.domain.entity.ColCategory;
import com.togedocs.backend.domain.entity.ColDto;
import com.togedocs.backend.domain.repository.ApidocsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApidocsService {
    private final ApidocsRepository apidocsRepository;
    private final String DEFAULT_TYPE = "text";
    private final int DEFAULT_WIDTH = 100;

    private final List<ColDto> DEFAULT_COLS = new ArrayList<>() {
        {
            add(new ColDto("one", "Name", DEFAULT_TYPE, DEFAULT_WIDTH, ColCategory.REQUIRED));
            add(new ColDto("two", "Method", DEFAULT_TYPE, DEFAULT_WIDTH, ColCategory.REQUIRED));
            add(new ColDto("three", "URL", DEFAULT_TYPE, DEFAULT_WIDTH, ColCategory.REQUIRED));
            add(new ColDto("d-one", "Query Params", DEFAULT_TYPE, 1, ColCategory.PAYLOAD));
            add(new ColDto("d-two", "Request Body", DEFAULT_TYPE, 1, ColCategory.PAYLOAD));
            add(new ColDto("d-three", "Response Body", DEFAULT_TYPE, 1, ColCategory.PAYLOAD));
        }
    };

    public void createApidocs(ProjectRequest.CreateProjectRequest request, Long projectId) {
        Apidocs apidocs = Apidocs.builder().projectId(projectId).title(request.getTitle()).desc(request.getDesc()).rows(new ArrayList<>()).cols(DEFAULT_COLS).data(new HashMap<>()).build();
        apidocsRepository.createApidocs(apidocs);
    }

    public void deleteApidocs(Long projectId) {
        apidocsRepository.deleteApidocs(projectId);
    }

    public void addRow(Long projectId) {
        apidocsRepository.addRow(projectId);
    }

    public void addCol(Long projectId, ApidocsRequest.AddColRequest request) {
        String colId = UUID.randomUUID().toString();
        ColDto colDto = ColDto.build(colId, request.getName(), request.getType(), DEFAULT_WIDTH, ColCategory.ADDED);
        apidocsRepository.addCol(projectId, colDto);
    }

    public void moveRow(Long projectId, ApidocsRequest.MoveItemRequest request) {
        apidocsRepository.moveRow(projectId, request);
    }

    public void moveCol(Long projectId, ApidocsRequest.MoveItemRequest request) {
        apidocsRepository.moveCol(projectId, request);
    }

    public void deleteRow(Long projectId, String rowId) {
        apidocsRepository.deleteRow(projectId, rowId);
    }

    public void deleteCol(Long projectId, String colId) {
        if (colId.equals("one") || colId.equals("two") || colId.equals("three") || colId.equals("d-one") || colId.equals("d-two") || colId.equals("d-three")) {
            throw new BusinessException(ErrorCode.DELETE_COL_FORBIDDEN);
        }
        apidocsRepository.deleteCol(projectId, colId);
    }

    public void updateCell(Long projectId, ApidocsRequest.UpdateCellRequest request) {
        apidocsRepository.updateCell(projectId, request);
    }

    public ApidocsResponse.Apidocs getDocs(Long projectId) {
        Apidocs apidocs = apidocsRepository.getDocs(projectId);
        return ApidocsResponse.Apidocs.build(apidocs);
    }

    public void updateProjectInfo(Long projectId, ApidocsRequest.UpdateProjectInfoRequest request) {
        apidocsRepository.updateProjectInfo(projectId, request);
    }

    public void updateCol(Long projectId, String colId, ApidocsRequest.UpdateColRequest request) {
        apidocsRepository.updateCol(projectId, colId, request);
    }

}
