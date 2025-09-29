package me.leegiseok.project;

import me.leegiseok.project.config.jwt.JwtTokenProvider;
import me.leegiseok.project.dto.LoginRequest;
import me.leegiseok.project.dto.LoginResponse;
import me.leegiseok.project.dto.SignupRequest;
import me.leegiseok.project.exception.UnauthorizedException;
import me.leegiseok.project.repository.RefreshTokenRepository;
import me.leegiseok.project.service.AuthService;
import me.leegiseok.project.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class AuthMockingTest {

    @Autowired
    AuthService authService;
    @MockitoBean
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    UserService userService;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void setup() {
        doNothing().when(jwtTokenProvider).validate(anyString());
        when(jwtTokenProvider.getUsername(anyString())).thenReturn("user");
        when(jwtTokenProvider.createAccessToken(anyString())).thenReturn("AT_DEFAULT");
        when(jwtTokenProvider.createRefreshToken(anyString())).thenReturn("RT_DEFAULT");

    }

    @Test
    @DisplayName("rotate 실패 ")
    void rotatefail() {
        String rt = "looksvalid";
        assertThrows(UnauthorizedException.class,() -> authService.rotate(rt));

    }

    @Test
    @DisplayName("rotate 발급 예외")
    void rotateissue() {
        userService .signup(new SignupRequest("user","1234","nickname"));
        when(jwtTokenProvider.createRefreshToken("user")).thenReturn("RT_DEFAULT");
        when(jwtTokenProvider.createAccessToken("user")).thenReturn("AT_DEFAULT");
        LoginResponse response = userService.login(new LoginRequest("user","1234"));
        String oldrt =  response.getRefreshToken();

        when(jwtTokenProvider.createRefreshToken("user"))
                .thenThrow(new RuntimeException("issue"));

        assertThrows(RuntimeException.class, () -> authService.rotate(oldrt));
        assertThat(refreshTokenRepository.findByToken(oldrt)).isPresent();


    }
}
