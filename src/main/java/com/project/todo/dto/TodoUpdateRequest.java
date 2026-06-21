package com.project.todo.dto;

import com.project.todo.enums.StatusEnum;

public record TodoUpdateRequest(String title, StatusEnum status) {

}
