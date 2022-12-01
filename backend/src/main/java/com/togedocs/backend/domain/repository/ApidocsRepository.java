package com.togedocs.backend.domain.repository;


import com.togedocs.backend.api.dto.ApidocsRequest;
import com.togedocs.backend.domain.entity.Apidocs;
import com.togedocs.backend.domain.entity.ColDto;

public interface ApidocsRepository {
    boolean existsByProjectId(Long projectId);

    void createApidocs(Apidocs apidocs);

    void deleteApidocs(Long projectId);

    void addRow(Long projectId);

    void addCol(Long projectId, ColDto colDto);

    void moveRow(Long projectId, ApidocsRequest.MoveItemRequest request);

    void moveCol(Long projectId, ApidocsRequest.MoveItemRequest request);

    void deleteRow(Long projectId, String rowId);

    void deleteCol(Long projectId, String colId);

    void updateCell(Long projectId, ApidocsRequest.UpdateCellRequest request);

    Apidocs getDocs(Long projectId);

    void updateProjectInfo(Long projectId, ApidocsRequest.UpdateProjectInfoRequest request);

    void updateCol(Long projectId, String colId, ApidocsRequest.UpdateColRequest request);
}
