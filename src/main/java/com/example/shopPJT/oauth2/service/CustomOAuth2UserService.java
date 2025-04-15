package com.example.shopPJT.oauth2.service;

import com.example.shopPJT.oauth2.details.CustomOAuth2UserDetails;
import com.example.shopPJT.oauth2.details.GoogleUserDetails;
import com.example.shopPJT.oauth2.details.OAuth2UserInfo;
import com.example.shopPJT.user.entity.RoleType;
import com.example.shopPJT.user.entity.User;
import com.example.shopPJT.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    @Autowired
    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = null;

        if(provider.equals("google")) {
            oAuth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());
        }
        if(oAuth2UserInfo == null) {
            throw new OAuth2AuthenticationException("Unsupported provider: " + provider);
        }

        String oAuth2Id = provider + "_" + oAuth2UserInfo.getProviderId();
        Optional<User> findUser = userRepository.findByoAuth2Id(oAuth2Id);
        User user;
        if(findUser.isPresent()) {
            user = findUser.get();
        } else {
            // 소셜 로그인은 '일반 사용자'만 가능
            user = new User();
            user.setoAuth2Id(oAuth2Id);
            user.setEmail(oAuth2UserInfo.getEmail());
            user.setName(oAuth2UserInfo.getName());
            user.setPassword("OAuth2User");
            user.setRole(RoleType.ROLE_USER);
            userRepository.save(user);
        }
        return new CustomOAuth2UserDetails(user, oAuth2User.getAttributes());
    }
}
