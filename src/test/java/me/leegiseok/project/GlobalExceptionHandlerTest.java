package me.leegiseok.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.leegiseok.project.advice.GlobalExceptionHandler;
import me.leegiseok.project.config.jwt.JwtAuthFilter;
import me.leegiseok.project.config.jwt.JwtTokenProvider;
import me.leegiseok.project.controller.ErrorController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = {ErrorController.class, GlobalExceptionHandler.class})
public class GlobalExceptionHandlerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    JwtTokenProvider jwtTokenProvider;
    @MockitoBean
    JwtAuthFilter jwtAuthFilter;

    @Test
    void forbidden_403() throws  Exception {

        mockMvc.perform(get("/test/errors/forbidden"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("FORBIDDEN"));
    }

    @Test
    void runtime_500() throws  Exception{
        mockMvc.perform(get("/test/errors/boom"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("INTERNAL_ERROR"));

    }
    @Test
    void unauthorized_401() throws Exception{
        mockMvc.perform(get("/test/errors/unauth"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.path").value("/test/errors/unauth"));

    }
    @Test
    void illegalArgument_mapped_to_404() throws Exception {
        mockMvc.perform(get("/test/errors/notfound"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

  /*  @Test
    void bodyValidation_400() throws Exception {
        // name이 @NotBlank라 빈 문자열로 실패 유도
        var bad = new SampleDto("");
        mockMvc.perform(post("/test/errors/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").exists());
    } */


}
