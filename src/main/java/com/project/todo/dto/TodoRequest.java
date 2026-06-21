package com.project.todo.dto;

import jakarta.validation.constraints.NotBlank;

public record TodoRequest(@NotBlank String title) {

}
