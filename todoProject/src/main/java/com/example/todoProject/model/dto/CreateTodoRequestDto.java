package com.example.todoProject.model.dto;

import lombok.Getter;

@Getter
public class CreateTodoRequestDto {
    private String contents;
    private String status;
}