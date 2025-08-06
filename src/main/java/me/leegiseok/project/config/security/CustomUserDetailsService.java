package me.leegiseok.project.config.security;

import lombok.RequiredArgsConstructor;
import me.leegiseok.project.domain.User;
import me.leegiseok.project.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private  final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("해당 사용자는 존재 하지 안흥ㅁ"));
        System.out.println(">> [로그인 시도] username: " + username);
        System.out.println(">> [DB 저장 비밀번호]: " + user.getPassword());

        return new CustomUserDetails(user);


    }
}
