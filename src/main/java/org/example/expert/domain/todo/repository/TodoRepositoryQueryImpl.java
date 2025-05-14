package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;

import org.example.expert.domain.todo.dto.TodoFindCond;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.web.method.support.CompositeUriComponentsContributor;

@Repository
@RequiredArgsConstructor

public class TodoRepositoryQueryImpl implements TodoRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;
    private final TodoRepository todoRepository;
    private final CompositeUriComponentsContributor compositeUriComponentsContributor;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        return Optional.ofNullable(
            jpaQueryFactory.selectFrom(todo).leftJoin(todo.user).where(todo.id.eq(todoId))
                .fetchFirst());
    }

    @Override
    public Page<TodoResponse> searchTodos(Pageable pageable, TodoFindCond cond) {
        //동적쿼리생성
        BooleanBuilder builder = new BooleanBuilder();
        if (cond.getType() != null  && cond.getKeyword() != null) {
            switch (cond.getType()) {
                case TITLE:
                    builder.and(todo.title.contains(cond.getKeyword())); // contains 는 Like %% 와 같음
                    break;
                case NICKNAME:
                    builder.and(todo.user.userName.contains(cond.getKeyword()));
                    break;
            }
        }
        if (cond.getStartAt() != null && cond.getEndAt() != null) {
            builder.and(todo.createdAt.between(cond.getStartAt(), cond.getEndAt()));
        }
        List<TodoResponse> content = jpaQueryFactory.select(Projections.fields(
                    TodoResponse.class,
                    todo.title,
                    ExpressionUtils.as(
                        JPAExpressions.select(manager.count()).from(manager) // 매니저 수
                            .where(manager.todo.id.eq(todo.id)), "managerCount"
                    ),
                    ExpressionUtils.as(
                        JPAExpressions.select(comment.count()).from(comment) // 댓글 수
                            .where(comment.todo.id.eq(todo.id)), "commentCount"
                    )
                )
            ).from(todo)
            .where(builder)
            .offset(pageable.getOffset()).limit(pageable.getPageSize())
            .orderBy(todo.createdAt.desc())
            .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
            .select(todo.count())
            .from(todo)
            .where(builder);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

}

