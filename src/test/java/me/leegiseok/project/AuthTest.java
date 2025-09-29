package me.leegiseok.project;


import me.leegiseok.project.dto.LoginRequest;
import me.leegiseok.project.dto.LoginResponse;
import me.leegiseok.project.dto.RefreshResponse;
import me.leegiseok.project.dto.SignupRequest;
import me.leegiseok.project.exception.UnauthorizedException;
import me.leegiseok.project.repository.RefreshTokenRepository;

import me.leegiseok.project.repository.UserRepository;
import me.leegiseok.project.service.AuthService;
import me.leegiseok.project.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class AuthTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;



    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        SignupRequest request = new SignupRequest("user","1234","nickname");
        userService.signup(request);

    }



    @Test
    @DisplayName("토큰 발급 성공")
    void token() {

        LoginRequest loginRequest = new LoginRequest("user","1234");
        LoginResponse loginResponse = userService.login(loginRequest);


        assertThat(refreshTokenRepository.findByToken(loginResponse.getRefreshToken())).isNotNull();






    }

    @Test
    @DisplayName("토큰 재발급 후 삭제")
    void refresh() {

        LoginRequest loginRequest = new LoginRequest("user", "1234");
        LoginResponse loginResponse = userService.login(loginRequest);

        String rt = loginResponse.getRefreshToken();

        RefreshResponse roted=  authService.rotate(rt);

        authService.logout(roted.refreshToken());

        assertThat(refreshTokenRepository.findByToken(rt)).isEmpty();
        assertThat(refreshTokenRepository.findByToken(roted.refreshToken())).isEmpty();



    }

    @Test
    @DisplayName("토큰 발급 실패")
    void tokenfail() {


        LoginRequest loginRequest = new LoginRequest("user","1234");



       assertThrows(UnauthorizedException.class, () -> authService.rotate("1234"));





    }

    @Test
    @DisplayName("로그아웃")
    void logout() {

        LoginRequest request1 = new LoginRequest("user","1234");

        LoginResponse response=  userService.login(request1);
       authService.logout(response.getRefreshToken());

        assertThat(refreshTokenRepository.findByToken(response.getRefreshToken())).isEmpty();



    }

    @Test
    @DisplayName("전체 로그아웃")
    void alllogout() {


        LoginRequest request1 = new LoginRequest("user", "1234");
        LoginResponse response = userService.login(request1);
        LoginResponse response1 = userService.login(request1);

        String rt = response.getRefreshToken();
        String rt2 = response1.getRefreshToken();

        Long userId = userRepository.findByUsername("user")
                        .orElseThrow().getId();


        authService.logoutAll(userId);

        assertThat(refreshTokenRepository.findByToken(rt)).isEmpty();
       assertThat(refreshTokenRepository.findByToken(rt2)).isEmpty();

    }

    @Test
    @DisplayName("토큰 소멸 확인 ")
    void otherlogout() {


        LoginRequest request1 = new LoginRequest("user","1234");
        LoginResponse response = userService.login(request1);
        String rt = response.getRefreshToken();

        authService.logout(rt);
        assertDoesNotThrow(() -> authService.logout(rt));
        assertThat(refreshTokenRepository.findByToken(rt)).isEmpty();

    }




}
