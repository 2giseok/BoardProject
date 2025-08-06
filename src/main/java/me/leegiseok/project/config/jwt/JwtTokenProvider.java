package me.leegiseok.project.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;


import java.util.Date;

@Component
public class JwtTokenProvider {
    private  static final String SECRET_KEY = "123456789";
    private  static final long ACCESS_TOKEN_VALIDITY = 1000*60 * 30;
    private  static  final long REFRESH_TOKEN_VALIDITY= 1000L * 60 * 60 * 24 * 14;

    public  String createAccessToken(String username) {
        return createToken(username, ACCESS_TOKEN_VALIDITY);
    }

    public String createRefreshToken(String username) {
        return createToken(username, REFRESH_TOKEN_VALIDITY);
    }

    private String createToken(String username, long tokenmills) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime()+tokenmills);

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

    }


}
