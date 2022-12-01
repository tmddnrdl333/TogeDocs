package com.togedocs.backend.domain.repository;

import com.togedocs.backend.api.dto.ApilogsRequest;
import com.togedocs.backend.domain.entity.Apilogs;
import com.togedocs.backend.domain.entity.LogDto;

import java.util.List;

public interface ApilogsRepository {

    void createApilogs(Apilogs apilogs);

    void deleteApilogs(Long projectId);

    List<LogDto> getLogs(Long projectId, String rowId);

    void addLog(Long projectId, String rowId, LogDto logDto);
}