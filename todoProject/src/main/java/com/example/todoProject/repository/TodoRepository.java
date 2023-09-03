package com.example.todoProject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.todoProject.model.entity.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
	List<Todo> findByMemberIdOrderByCreatedTimestampDesc(Long memberId);
	Todo findFirstByMemberIdOrderByCreatedTimestampDesc(Long memberId);
	Optional<Todo> findByIdAndMemberId(long todoId, long memberId);
	void deleteByMemberId(Long memberId);
}