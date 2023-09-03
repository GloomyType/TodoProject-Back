package com.example.todoProject.model.dto;

import com.example.todoProject.model.entity.Todo;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoResponseDto {
    private Long todoId;
    private String contents;
    private String status;
    private String memberNickname;
    
    public static TodoResponseDto of(Todo todo) {
        return TodoResponseDto.builder()
        		.todoId(todo.getId())
                .memberNickname(todo.getMember().getNickname())
                .contents(todo.getContents())
                .status(todo.getStatus())
                .build();
    }
}