package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import static org.example.expert.domain.todo.entity.QTodo.todo;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor

public class TodoRepositoryQueryImpl implements TodoRepositoryQuery{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(todo).leftJoin(todo.user).where(todo.id.eq(todo.user.id)).fetchFirst());
    }
}

