package com.project.todo.dto;

import com.project.todo.enums.RoleEnum;

public record UserResponse(Long id, String email, RoleEnum role) {
}
