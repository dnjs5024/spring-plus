package org.example.expert.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class SigninResponse {

    private final String userName;
    private final String bearerToken;

    public SigninResponse(String bearerToken, String userName) {
        this.bearerToken = bearerToken;
        this.userName = userName;
    }
}
