package me.leegiseok.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.leegiseok.project.dto.*;
import me.leegiseok.project.service.AuthService;
import me.leegiseok.project.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private  final UserService userService;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request ) {
        SignupResponse response = userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PostMapping("/login")
    public  ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(@RequestBody @Valid RefreshRequest request) {

        var tokens = authService.rotate(request.refreshToken());
        return ResponseEntity.ok(tokens);

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshRequest request) {
        authService.logout(request.refreshToken());
        return  ResponseEntity.noContent().build();
    }
}
