package com.example.todoProject.model.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String contents;

    @Column(nullable = false)
    @NotBlank
    private String status;
    
    @Column(nullable = false)
    private LocalDateTime createdTimestamp;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Todo createTodo(String contents, String status, Member member) {
        Todo todo = new Todo();
        todo.contents = contents;
        todo.status = status;
        todo.member = member;
        todo.createdTimestamp = LocalDateTime.now(); // 현재 시간을 저장

        return todo;
    }

    public static Todo updateTodoStatus (long id, String status, Member member) {
        Todo Todo = new Todo();
        Todo.id = id;
        Todo.status = status;
        Todo.member = member;

        return Todo;
    }
}