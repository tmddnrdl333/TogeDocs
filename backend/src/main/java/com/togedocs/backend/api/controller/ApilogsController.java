package com.togedocs.backend.api.controller;

import com.togedocs.backend.api.dto.ApilogsRequest;
import com.togedocs.backend.api.dto.ApilogsResponse;
import com.togedocs.backend.api.service.ApilogsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/logs")
public class ApilogsController {
    private final ApilogsService apilogsService;

    @GetMapping("/{projectId}/{rowId}")
    public ResponseEntity<ApilogsResponse.Logs> getLogs(@PathVariable Long projectId, @PathVariable String rowId) {
        ApilogsResponse.Logs response = apilogsService.getLogs(projectId, rowId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{projectId}/{rowId}")
    public ResponseEntity<String> addLog(@PathVariable Long projectId, @PathVariable String rowId, @RequestBody @Valid ApilogsRequest.AddLogRequest request) {
        apilogsService.addLog(projectId, rowId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Add Log Successfully!");
    }

}
