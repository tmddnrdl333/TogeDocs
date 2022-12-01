package com.togedocs.backend.api.service;

import com.togedocs.backend.api.dto.ApilogsRequest;
import com.togedocs.backend.api.dto.ApilogsResponse;
import com.togedocs.backend.domain.entity.Apilogs;
import com.togedocs.backend.domain.entity.LogDto;
import com.togedocs.backend.domain.repository.ApilogsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class ApilogsService {
    private final ApilogsRepository apilogsRepository;

    public void createApilogs(Long projectId) {
        Apilogs apilogs = Apilogs.builder().projectId(projectId).log(new HashMap<>()).build();
        apilogsRepository.createApilogs(apilogs);
    }

    public void deleteApilogs(Long projectId) {
        apilogsRepository.deleteApilogs(projectId);
    }

    public ApilogsResponse.Logs getLogs(Long projectId, String rowId) {
        return ApilogsResponse.Logs.build(apilogsRepository.getLogs(projectId, rowId));
    }

    public void addLog(Long projectId, String rowId, ApilogsRequest.AddLogRequest request) {
        String logTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toString();
        LogDto logDto = LogDto.build(logTime, request.getUserName(), request.getMethod(), request.getUrl(), request.getRequestBody(), request.getStatusCode(), request.getResponseBody());
        apilogsRepository.addLog(projectId, rowId, logDto);
    }
}
