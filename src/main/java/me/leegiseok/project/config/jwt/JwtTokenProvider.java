package me.leegiseok.project.config.jwt;

import com.nimbusds.oauth2.sdk.auth.Secret;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private  final SecretKey key;
    private  static  final long ACCESS_TOKEN_VALIDITY= 30 * 60 * 1000L;
    private  static  final  Long REFRESH_TOKEN_VALIDITY = 14L * 24 * 60 * 60 * 10;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(secret);
        } catch ( IllegalArgumentException e) {
            keyBytes= secret.getBytes(StandardCharsets.UTF_8);

        } if (keyBytes.length < 32) {
            throw  new IllegalArgumentException("JWTT secret must bo at least 32bytes for HS256 ");


        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public  String createAccessToken( String username) {

        return  createToken(username, ACCESS_TOKEN_VALIDITY);
    }
    public  String createRefreshToken(String username) {
        return  createToken(username, REFRESH_TOKEN_VALIDITY);

    }
    private  String createToken(String username, long validityMs) {
        Date now = new Date();
        Date expiry = new Date(now.getTime()+ validityMs);

        return  Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();
    }

    public  String getUsername( String token) {
        return  Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();

    }
    public  void validate(String token) {

        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

}
