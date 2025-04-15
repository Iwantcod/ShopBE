package com.example.shopPJT.user.details;

import com.example.shopPJT.user.dto.AuthDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
// JwtUserDetailsService를 통해 데이터베이스에서 가져온 실제 유저 정보를 담는 틀
public class JwtUserDetails implements UserDetails {
    private final AuthDto authDto;
    public JwtUserDetails(AuthDto authDto) {
        this.authDto = authDto;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return authDto.getRoleType();
            }
        });
        return authorities;
    }

    public Long getUserId() {
        return authDto.getUserId();
    }

    @Override
    public String getPassword() {
        return authDto.getPassword();
    }

    @Override
    public String getUsername() {
        return authDto.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
