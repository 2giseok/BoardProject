package me.leegiseok.project.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.leegiseok.project.config.jwt.JwtTokenProvider;
import me.leegiseok.project.domain.RefreshToken;
import me.leegiseok.project.dto.RefreshResponse;
import me.leegiseok.project.exception.UnauthorizedException;
import me.leegiseok.project.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AuthService {

    private  final JwtTokenProvider jwtTokenProvider;
    private  final RefreshTokenRepository refreshTokenRepository;


    @Transactional
    public RefreshResponse rotate( String refreshToken) {
        try {
            jwtTokenProvider.validate(refreshToken);
        } catch ( Exception e ) {
            throw  new UnauthorizedException("Invalid or expired refresh token");

        }
        var rt =  refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new UnauthorizedException("Refreshtoken not found"));

        String username = jwtTokenProvider.getUsername(refreshToken);

        refreshTokenRepository.delete(rt);
        String newAccess = jwtTokenProvider.createAccessToken(username);
        String newRefresh = jwtTokenProvider.createRefreshToken(username);

        refreshTokenRepository.save(new RefreshToken(rt.getUserId(), newRefresh));
        return new RefreshResponse(newAccess,newRefresh);

    }

    @Transactional
    public  void logout (String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }


    @Transactional
    public void logoutAll(Long userId) {
        refreshTokenRepository.findById(userId)
                .ifPresent(refreshTokenRepository::delete);

    }
}
