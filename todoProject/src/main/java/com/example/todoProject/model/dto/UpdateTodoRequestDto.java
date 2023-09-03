package com.example.todoProject.model.dto;

import lombok.Getter;

@Getter
public class UpdateTodoRequestDto {
    private long todoId;
    private String status;
}