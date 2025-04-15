package com.example.shopPJT.user.service;

import com.example.shopPJT.user.details.JwtUserDetails;
import com.example.shopPJT.user.dto.AuthDto;
import com.example.shopPJT.user.entity.User;
import com.example.shopPJT.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // 로그인 정보를 데이터베이스와 비교하여 일치하는지 검증하는 서비스 빈
public class JwtUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Autowired
    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override // 이메일을 통해 유저를 조회한 결과를 AuthDto 인스턴스에 담고, JwtUserDetails 생성자의 매개변수로 담아 반환
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);

        if(user.isPresent()) {
            AuthDto authDto = new AuthDto();
            authDto.setUserId(user.get().getId());
            authDto.setEmail(user.get().getEmail());
            authDto.setPassword(user.get().getPassword());
            authDto.setRoleType(user.get().getRole().toString());
            return new JwtUserDetails(authDto);
        } else {
            return null;
        }
    }
}
