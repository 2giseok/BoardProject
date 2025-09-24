package me.leegiseok.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.leegiseok.project.config.jwt.JwtAuthFilter;
import me.leegiseok.project.config.jwt.JwtTokenProvider;
import me.leegiseok.project.controller.UserController;
import me.leegiseok.project.dto.LoginRequest;
import me.leegiseok.project.dto.LoginResponse;
import me.leegiseok.project.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    UserController userController;
    @MockitoBean
    JwtAuthFilter jwtAuthFilter;
    @MockitoBean
    JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess()throws  Exception {
        LoginRequest request = new LoginRequest("user","1234");
        LoginResponse response = new LoginResponse("AT_TOKEN","RT_TOKEN","1234");
        given(userService.login(any(LoginRequest.class))).willReturn(response);

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.nickname").value("1234"))
                .andDo(print());


    }

    @Test
    @DisplayName("로그인 실패")
    void loginFail() {


    }
    @Test
    @DisplayName("회원가입Get")
    void signGet() {


    }
    @Test
    @DisplayName("회원가입 Post")
    void  signPost() {


    }


}
