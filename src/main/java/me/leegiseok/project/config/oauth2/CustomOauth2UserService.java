package me.leegiseok.project.config.oauth2;

import lombok.RequiredArgsConstructor;
import me.leegiseok.project.domain.User;
import me.leegiseok.project.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    private  final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);

        String registraionId = request.getClientRegistration().getRegistrationId();
        String userNameAttributeName = request.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        Map<String ,Object> attributes=  oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String)attributes.get("name");

      User user =  userRepository.findByUsername(email)
                .orElseGet(( ) -> userRepository.save(User.builder()
                        .username(email)
                        .password(" ")
                        .nickname(name)
                        .role("ROLE_USER")
                        .build()
                ));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole())),
                attributes,
                userNameAttributeName

        );
    }


}
