package org.example.expert.domain.todo.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class TodoFindCond {

    private String weather;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String keyword;
    private TYPE type;

    public enum TYPE{
        TITLE,
        NICKNAME
    }
}
