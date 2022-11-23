package com.togedocs.backend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 400 BAD_REQUEST

    // 401 UNAUTHORIZED

    // 404 NOT_FOUND
    PROJECT_NOT_FOUND(NOT_FOUND, "프로젝트를 찾을 수 없습니다."),
    ROW_NOT_FOUND(NOT_FOUND, "행을 찾을 수 없습니다."),
    COL_NOT_FOUND(NOT_FOUND, "열을 찾을 수 없습니다."),
    USER_NOT_FOUND(NOT_FOUND,"유저를 찾을 수 없습니다."),
    PROJECT_USER_NOT_FOUND(NOT_FOUND, "유저가 프로젝트에 속하지 않습니다.");

    private final HttpStatus status;
    private final String msg;
}
