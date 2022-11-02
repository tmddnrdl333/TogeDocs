package com.togedocs.backend.api.controller;

import com.togedocs.backend.api.dto.ApidocsRequest;
import com.togedocs.backend.api.dto.ApidocsResponse;
import com.togedocs.backend.api.service.ApidocsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/docs")
public class ApidocsController {

    @Autowired
    private ApidocsService apidocsService;

    @PostMapping("/{projectId}/rows")
    public ResponseEntity<ApidocsResponse.Ids> addRow(@PathVariable Long projectId){
        ApidocsResponse.Ids response = apidocsService.addRow(projectId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/{projectId}/cols")
    public ResponseEntity<?> addCol(@PathVariable Long projectId, @RequestBody ApidocsRequest.AddColRequest request){
        ApidocsResponse.Ids response = apidocsService.addCol(projectId, request);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{projectId}/rows")
    public ResponseEntity<?> moveRow(@PathVariable Long projectId, @RequestBody ApidocsRequest.MoveItemRequest request){
        ApidocsResponse.Ids response = apidocsService.moveRow(projectId, request);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{projectId}/cols")
    public ResponseEntity<?> moveCol(@PathVariable Long projectId, @RequestBody ApidocsRequest.MoveItemRequest request){
        ApidocsResponse.Ids response = apidocsService.moveCol(projectId, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{projectId}/rows/{rowId}")
    public ResponseEntity<ApidocsResponse.Ids> deleteRow(@PathVariable Long projectId, @PathVariable String rowId) {
        ApidocsResponse.Ids response = apidocsService.deleteRow(projectId, rowId);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{projectId}/cols/{colId}")
    public ResponseEntity<?> deleteCol(@PathVariable Long projectId, @PathVariable String colId) {
        ApidocsResponse.Ids response = apidocsService.deleteCol(projectId, colId);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{projectId}")
    public ResponseEntity<?> updateCell(@PathVariable Long projectId, @RequestBody ApidocsRequest.UpdateCellRequest request) {
        ApidocsResponse.Ids response = apidocsService.updateCell(projectId, request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ApidocsResponse.Apidocs> getDocs(@PathVariable Long projectId) {
        ApidocsResponse.Apidocs response = apidocsService.getDocs(projectId);
        return ResponseEntity.ok().body(response);
    }
}