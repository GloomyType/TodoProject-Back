package com.example.todoProject.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.example.todoProject.config.SecurityUtil;
import com.example.todoProject.model.dto.TodoResponseDto;
import com.example.todoProject.model.entity.Member;
import com.example.todoProject.model.entity.Todo;
import com.example.todoProject.repository.MemberRepository;
import com.example.todoProject.repository.TodoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoService {
	
	private final MemberRepository memberRepository;
	private final TodoRepository todoRepository;
	
	//TodoList 가져오기
	@Transactional
	public List<TodoResponseDto> getTodoList() {
	    Member member = isMemberCurrent();
	    List<Todo> todoList = todoRepository.findByMemberIdOrderByCreatedTimestampDesc(member.getId());
	    return todoList.stream()
	            .map(TodoResponseDto::of)
	            .collect(Collectors.toList());
	}
	
	//TodoList 최신한개 가져오기
	@Transactional
	public TodoResponseDto getTodoOne() {
	    Member member = isMemberCurrent();
	    return TodoResponseDto.of(todoRepository.findFirstByMemberIdOrderByCreatedTimestampDesc(member.getId()));
	}
	
	//TodoList 등록
	@Transactional
    public TodoResponseDto postTodo(String contents, String status) {
        Member member = isMemberCurrent();
        Todo todo = Todo.createTodo(contents, status, member);
        return TodoResponseDto.of(todoRepository.save(todo));
    }
	
	//TodoList 상태변경
	@Transactional
	public TodoResponseDto updateTodoStatus(long todoId, String status) {
	    Member member = isMemberCurrent();
	    Optional<Todo> todoOptional = todoRepository.findByIdAndMemberId(todoId, member.getId());
	    if (todoOptional.isPresent()) {
	        Todo todo = todoOptional.get();
	        todo.setStatus(status); // newStatus에 변경할 상태 값을 넣어주세요.
	        Todo updatedTodo = todoRepository.save(todo);        
	        return TodoResponseDto.of(updatedTodo);
	    } else {
	        throw new RuntimeException("해당 Todo를 찾을 수 없습니다.");
	    }
	}
	
	//TodoList 토큰(헤더에 있음) 기반 유저정보 가져오기
    public Member isMemberCurrent() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }

}