package org.example.expert.domain.todo.repository;

import java.time.LocalDateTime;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u WHERE "
        + "(:weather is null  or t.weather = :weather) "
        + "AND (:startAt is null or t.modifiedAt > :startAt) "
        + "AND (:endAt is null or t.modifiedAt < :endAt) "
        + "ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByDateAndWeatherOrderByModifiedAtDesc(
        Pageable pageable,
        @Param(value = "weather") String weather,
        @Param(value = "startAt") LocalDateTime startAt,
        @Param(value = "endAt") LocalDateTime endAt
        );

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN t.user " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);
}
