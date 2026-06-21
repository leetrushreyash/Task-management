package com.project.todo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequest(@Email @NotBlank String email,
        @Size(min = 8, message = "password must be atleast 8 characters long") String password) {
}
