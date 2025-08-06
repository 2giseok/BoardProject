package me.leegiseok.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupResponse {
    private  Long id;

    private  String username;
    private  String nickname;
}
