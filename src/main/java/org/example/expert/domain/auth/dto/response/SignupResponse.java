package org.example.expert.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class SignupResponse {

    private final String userName;

    public SignupResponse(String userName) {
        this.userName = userName;
    }
}
