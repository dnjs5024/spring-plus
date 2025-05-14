package org.example.expert.domain.user.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserFindCond {

    @NotBlank
    private String name;

}
