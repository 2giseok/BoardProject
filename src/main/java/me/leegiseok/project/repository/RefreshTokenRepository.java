package me.leegiseok.project.repository;


import me.leegiseok.project.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);

    Optional<RefreshToken>findById(Long Id);

}
