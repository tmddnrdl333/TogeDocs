package com.togedocs.backend.api.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public class UserRequest {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ModifyUserRequest {
        @NotEmpty
        private String name;
        @Positive
        private int imgNo;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class UserInfoRequest {
        // TODO : FE에서 Req를 바꿔야함.
        private String email;
        private String uuid;
        private int imgNo;
        private String name;
    }
}
