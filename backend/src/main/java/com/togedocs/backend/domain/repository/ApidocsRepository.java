package com.togedocs.backend.domain.repository;


import com.togedocs.backend.api.dto.ApidocsRequest;
import com.togedocs.backend.domain.entity.Apidocs;

public interface ApidocsRepository {
    boolean existsByProjectId(Long projectId);

    void createApidocs(Apidocs apidocs);

    void deleteApidocs(Long projectId);

    void addRow(Long projectId);

    void addCol(Long projectId, ApidocsRequest.AddColRequest request);

    boolean moveRow(Long projectId, ApidocsRequest.MoveItemRequest request);

    boolean moveCol(Long projectId, ApidocsRequest.MoveItemRequest request);

    boolean deleteRow(Long projectId, String rowId);

    boolean deleteCol(Long projectId, String colId);

    boolean updateCell(Long projectId, ApidocsRequest.UpdateCellRequest request);

    Apidocs getDocs(Long projectId);

    boolean updateProjectInfo(Long projectId, ApidocsRequest.UpdateProjectInfoRequest request);

    boolean updateCol(Long projectId, String colId, ApidocsRequest.UpdateColRequest request);
}
