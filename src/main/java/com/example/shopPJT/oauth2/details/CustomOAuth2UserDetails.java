package com.example.shopPJT.oauth2.details;

import com.example.shopPJT.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2UserDetails implements OAuth2User {
    private final User user;
    private Map<String, Object> attributes;
    public CustomOAuth2UserDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole().name();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return user.getName();
    }

    public String getOAuth2Id() {
        return user.getOAuth2Id();
    }

    public User getUser() {
        return user;
    }
}
