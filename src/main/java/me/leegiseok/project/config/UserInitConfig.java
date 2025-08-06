package me.leegiseok.project.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import me.leegiseok.project.domain.User;
import me.leegiseok.project.repository.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class UserInitConfig {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (userRepository.findByUsername("testuser").isEmpty()) {
            User user = User.builder()
                    .username("testuser")
                    .password(passwordEncoder.encode("1234"))
                    .nickname("테스터")
                    .role("ROLE_USER")
                    .build();
            userRepository.save(user);
            System.out.println("✅ testuser 계정이 생성되었습니다 (비밀번호: 1234)");
        }
    }
}
