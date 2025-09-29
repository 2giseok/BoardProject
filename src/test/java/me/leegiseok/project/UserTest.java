package me.leegiseok.project;

import me.leegiseok.project.domain.User;
import me.leegiseok.project.dto.LoginRequest;
import me.leegiseok.project.dto.LoginResponse;
import me.leegiseok.project.dto.SignupRequest;
import me.leegiseok.project.dto.SignupResponse;
import me.leegiseok.project.repository.UserRepository;

import me.leegiseok.project.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class UserTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;


    @Test
    @DisplayName("회원 가입 ")
    void signup() {
        SignupRequest request = new SignupRequest("id123", " password", "nickname");

        SignupResponse saved  = userService.signup(request);


        assertThat(saved.getUsername()).isEqualTo("id123");


    }

    @Test
    @DisplayName("로그인")
    void login() {
        SignupRequest request = new SignupRequest("id123", "password", "nickname");

      userService.signup(request);

      LoginRequest request1 = new LoginRequest("id123","password" );
        LoginResponse response = userService.login(request1);
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isNotBlank();


    }
    @Test
    @DisplayName("다른 비밀번호 로그인")
    void otherpassword() {
        SignupRequest request = new SignupRequest("id123","password","nickname");
        userService.signup(request);
        LoginRequest request1 = new LoginRequest("id123", "fail");
        assertThrows(IllegalArgumentException.class, () -> userService.login(request1));




    }

    @Test
    @DisplayName("존재하지 않는 로그인 ")
    void idfail() {
        LoginRequest request = new LoginRequest("id123", "fail");
        assertThrows(IllegalArgumentException.class, () -> userService.login(request));

    }

}
