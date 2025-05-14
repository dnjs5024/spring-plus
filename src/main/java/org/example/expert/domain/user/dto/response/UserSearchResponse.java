package org.example.expert.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserSearchResponse {

    private final Long id;

    private final String email;

    private final String userName;

    public UserSearchResponse(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.userName = name;
    }
}
