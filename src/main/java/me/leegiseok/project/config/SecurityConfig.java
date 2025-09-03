package me.leegiseok.project.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import me.leegiseok.project.config.jwt.JwtAuthFilter;
import me.leegiseok.project.config.jwt.JwtTokenProvider;
import me.leegiseok.project.config.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@RequiredArgsConstructor

public class SecurityConfig {
    private  final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws  Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(reg -> reg
                        .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**","/swagger-ui.html").permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .userDetailsService(customUserDetailsService)
        .addFilterBefore(jwtAuthFilter,org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return  http.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig
    ) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
   public CorsConfigurationSource corsConfigurationSource() {
        var conf =new CorsConfiguration();
        conf.setAllowedOrigins(List.of("http://localhost:3000","http://localhost:5173"));
        conf.setAllowedMethods(List.of("GET","POST","PATCH","PUT","DELETE","OPTIONS"));
        conf.setAllowedHeaders(List.of("Authorization","Content-Type"));
        conf.setExposedHeaders(List.of("Authorization"));
        conf.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", conf);
        return source;

    }
}

