package me.leegiseok.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import me.leegiseok.project.config.jwt.JwtAuthFilter;
import me.leegiseok.project.config.jwt.JwtTokenProvider;
import me.leegiseok.project.controller.AuthController;
import me.leegiseok.project.dto.*;
import me.leegiseok.project.service.AuthService;
import me.leegiseok.project.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    UserService userService;
    @MockitoBean
    JwtAuthFilter jwtAuthFilter;
    @MockitoBean
    JwtTokenProvider jwtTokenProvider;


    @MockitoBean
    AuthService authService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 검증 ")
    void signup() throws  Exception{
        SignupRequest request = new SignupRequest("user","1234","nickname");
        SignupResponse response = new SignupResponse(1L,"user","nickname");

        given(userService.signup(any(SignupRequest.class))).willReturn(response);

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.nickname").value("nickname"))
                .andDo(print());
    }
    @Test
    @DisplayName("로그인 검증")
    void login() throws  Exception{

        LoginRequest loginRequest = new LoginRequest("user","1234");
        LoginResponse loginResponse = new LoginResponse("AT_TOKEN","RT_TOKEN","user");
        given(userService.login(any(LoginRequest.class))).willReturn(loginResponse);

         mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("AT_TOKEN"))
                .andExpect(jsonPath("$.refreshToken").value("RT_TOKEN"))
                .andDo(print());


    }

    @Test
    @DisplayName("로그아웃 검증")
    void logout() throws  Exception{
        RefreshRequest request = new RefreshRequest("RT_TOKEN");
        doNothing().when(authService).logout("RT_TOKEN");

        mockMvc.perform(post("/api/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""))
                .andDo(print());



    }

    @Test
    @DisplayName("토큰 재발급")
    void refresh() throws  Exception{
        RefreshRequest request = new RefreshRequest("RT_TOKEN");
        RefreshResponse response=  new RefreshResponse("NEW_ATTOKEN","new_RTTOKEN");


        given(authService.rotate(any(String.class))).willReturn(response);

        mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("NEW_ATTOKEN"))
                .andExpect(jsonPath("$.refreshToken").value("new_RTTOKEN"));



    }





    

}
