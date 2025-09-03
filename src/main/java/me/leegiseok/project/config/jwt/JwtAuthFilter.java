package me.leegiseok.project.config.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.leegiseok.project.config.security.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static  final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private  final  JwtTokenProvider jwtTokenProvider;
    private  final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if(!StringUtils.hasText(header) ||!header.startsWith("Bearer")) {
            chain.doFilter(request,response);
            return;
        }
        String token = header.substring(7);
        try {
            jwtTokenProvider.validate(token);
            String username = jwtTokenProvider.getUsername(token);
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                var user = userDetailsService.loadUserByUsername(username);
                var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        }catch (Exception e) {
            log.debug("JWT invalid: {}" , e.getMessage());
        }
chain.doFilter(request,response);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String  p = request.getServletPath();
        return p.startsWith("/api/auth")
                || p.startsWith("/swagger-ui")
                || p.startsWith("/v3/api-docs");

    }
}
