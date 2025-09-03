package me.leegiseok.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // ← 테스트는 'test' 프로필(H2)을 강제 사용
class SecureBoardApplicationTests {

    @Test
    void contextLoads() {
    }
}
