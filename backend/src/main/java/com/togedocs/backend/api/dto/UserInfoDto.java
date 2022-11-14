package com.togedocs.backend.api.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.togedocs.backend.domain.entity.ProjectUserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class UserInfoDto {
    private Long id;
    private String name;
    private ProjectUserRole role;

    @QueryProjection
    public UserInfoDto(Long id, String name, ProjectUserRole role){
        this.id = id;
        this.name = name;
        this.role = role;
    }
}
