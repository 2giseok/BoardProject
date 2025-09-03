package me.leegiseok.project;

import me.leegiseok.project.dto.LoginRequest;
import me.leegiseok.project.dto.LoginResponse;
import me.leegiseok.project.dto.RefreshResponse;
import me.leegiseok.project.dto.SignupRequest;
import me.leegiseok.project.repository.RefreshTokenRepository;

import me.leegiseok.project.service.AuthService;
import me.leegiseok.project.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class AuthTest {


    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    SignupRequest request = new SignupRequest("user","1234","nickname");



    @Test
    @DisplayName("토큰 발급 성공")
    void token() {
        userService.signup(request);
        LoginRequest loginRequest = new LoginRequest("user","1234");
        LoginResponse loginResponse = userService.login(loginRequest);

        RefreshResponse response = authService.rotate(loginResponse.getRefreshToken());
        assertThat(refreshTokenRepository.findByToken(loginResponse.getRefreshToken())).isNotNull();




    }

    @Test
    @DisplayName("토큰 발급 실패")
    void tokenfail() {
        userService.signup(request);
        LoginRequest loginRequest = new LoginRequest("user","1234");
        LoginResponse loginResponse = userService.login(loginRequest);




    }

    @Test
    @DisplayName("로그아웃")
    void logout() {

        userService.signup(request);

    }

    @Test
    @DisplayName("전체 로그아웃")
    void alllogout() {
        userService.signup(request);

    }

}
