package me.leegiseok.project.service;

import lombok.RequiredArgsConstructor;
import me.leegiseok.project.config.jwt.JwtTokenProvider;
import me.leegiseok.project.domain.RefreshToken;
import me.leegiseok.project.domain.User;
import me.leegiseok.project.dto.LoginRequest;
import me.leegiseok.project.dto.SignupRequest;
import me.leegiseok.project.repository.RefreshTokenRepository;
import me.leegiseok.project.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import me.leegiseok.project.dto.SignupResponse;
import me.leegiseok.project.dto.LoginResponse;
@Service
@RequiredArgsConstructor
public class UserService {
private  final RefreshTokenRepository refreshTokenRepository;
private  final UserRepository userRepository;
private  final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    public SignupResponse signup(SignupRequest request) {
//회원 가입
        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw  new IllegalArgumentException("이미 존재하는 아이디 입니다 ");


        }
        String encodePassword = passwordEncoder.encode(request.getPassword());

User user = User.builder().
        username(request.getUsername())
        .password(encodePassword)
        .nickname(request.getNickname())
        .role("ROLE_USER")

        .build();
User savedUser = userRepository.save(user);
return  new SignupResponse(
        savedUser.getId(),
        savedUser.getUsername()
        ,savedUser.getNickname()
);
    }

    //로그인
    public  LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(( ) -> new IllegalArgumentException("아이디 혹은 비밀번호가 다릅니다"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword() )) {
            throw  new IllegalArgumentException("비밀번호가 일치 하지 않습니다");
        }

        String accessToken =jwtTokenProvider.createAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername());

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .userId(user.getId())
                        .token(refreshToken)
                        .build()
        );
        return new LoginResponse(accessToken, refreshToken, user.getNickname());
    }






}
