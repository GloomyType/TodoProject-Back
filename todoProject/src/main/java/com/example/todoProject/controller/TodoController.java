package com.example.todoProject.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;

import com.example.todoProject.model.dto.CreateTodoRequestDto;
import com.example.todoProject.model.dto.TodoResponseDto;
import com.example.todoProject.model.dto.UpdateTodoRequestDto;
import com.example.todoProject.service.TodoService;

@RestController 
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @GetMapping("/")
    public ResponseEntity<List<TodoResponseDto>> getTodoList() {
        return ResponseEntity.ok(todoService.getTodoList());
    }
    
    @GetMapping("/latest")
    public ResponseEntity<TodoResponseDto> getTodoOne() {
        return ResponseEntity.ok(todoService.getTodoOne());
    }
    
    @PostMapping("/")
    public ResponseEntity<TodoResponseDto> createTodo(@RequestBody CreateTodoRequestDto request) {
        return ResponseEntity.ok(todoService.postTodo(request.getContents(), request.getStatus()));
    }

    @PutMapping("/")
    public ResponseEntity<TodoResponseDto> updateTodoStatus(@RequestBody UpdateTodoRequestDto request) {
        return ResponseEntity.ok(todoService.updateTodoStatus(request.getTodoId(), request.getStatus()));
    }
}