package org.example.expert.domain.todo.repository;

import java.util.Optional;
import org.example.expert.domain.todo.dto.TodoFindCond;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoRepositoryQuery {
    Optional<Todo> findByIdWithUser(Long todoId);

    Page<TodoResponse> searchTodos(Pageable pageable, TodoFindCond cond);
}