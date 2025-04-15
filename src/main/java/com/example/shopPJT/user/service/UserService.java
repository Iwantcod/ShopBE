package com.example.shopPJT.user.service;

import com.example.shopPJT.businessInfo.entity.BusinessInfo;
import com.example.shopPJT.businessInfo.repository.BusinessInfoRepository;
import com.example.shopPJT.user.dto.JoinSellerDto;
import com.example.shopPJT.user.dto.JoinUserDto;
import com.example.shopPJT.user.dto.ResUserDto;
import com.example.shopPJT.user.dto.UpdateUserDto;
import com.example.shopPJT.user.entity.User;
import com.example.shopPJT.user.repository.UserRepository;
import com.example.shopPJT.util.AuthUtil;
import com.example.shopPJT.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
    private final BusinessInfoRepository businessInfoRepository;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtUtil jwtUtil, BusinessInfoRepository businessInfoRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtil = jwtUtil;
        this.businessInfoRepository = businessInfoRepository;
    }

    // User -> ResUserDto 변환하는 메소드
    private ResUserDto toDto(User user) {
        ResUserDto resUserDto = new ResUserDto();
        resUserDto.setUserId(user.getId());
        resUserDto.setName(user.getName());
        resUserDto.setEmail(user.getEmail());
        resUserDto.setPhone(user.getPhone());
        resUserDto.setBirth(user.getBirth().toString());
        resUserDto.setAddress(user.getAddress());
        resUserDto.setZipCode(user.getZipCode());
        resUserDto.setRole(user.getRole().toString());
        return resUserDto;
    }


    // 이메일 중복 검사
    @Transactional(readOnly = true)
    public boolean isExistUser(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

    // 회원가입 공통로직
    private User basicJoin(JoinUserDto joinUserDto) {
        // 이메일, 비밀번호, 이름, 전화번호 중 하나라도 값을 전달받지 못하면 null 반환
        if(joinUserDto.getEmail() == null || joinUserDto.getPassword() == null
                || joinUserDto.getName() == null || joinUserDto.getPhone() == null) {
            return null;
        }
        User user = new User();
        user.setEmail(joinUserDto.getEmail());
        // 비밀번호는 암호화하여 DB에 저장
        String encodedPassword = bCryptPasswordEncoder.encode(joinUserDto.getPassword());
        user.setPassword(encodedPassword);
        user.setName(joinUserDto.getName());
        user.setAddress(joinUserDto.getAddress());
        user.setBirth(joinUserDto.getBirth());
        user.setPhone(joinUserDto.getPhone());
        user.setZipCode(joinUserDto.getZipCode());
        user.setRole(joinUserDto.getRole());
        return user;
    }

    // 기본 회원가입: 일반 사용자
    @Transactional
    public boolean join(JoinUserDto joinUserDto) {
        User user = basicJoin(joinUserDto);
        if(user == null){
            return false;
        } else {
            userRepository.save(user);
            return true;
        }

    }

    // 기본 회원가입: 판매자 유형
    @Transactional
    public boolean joinSeller(JoinSellerDto joinSellerDto) {
        User user = basicJoin(joinSellerDto);
        if(user == null){
            return false;
        }
        User savedUser = userRepository.save(user);  // 일반 사용자와 User 테이블에 입력되는 로직은 동일

        // BusinessInfo 테이블에 행을 추가한다.
        BusinessInfo businessInfo = new BusinessInfo();
        businessInfo.setUser(savedUser); // 회원 연관관계 매핑
        businessInfo.setBusinessType(joinSellerDto.getBusinessType());
        businessInfo.setBusinessNumber(joinSellerDto.getBusinessNumber());
        businessInfo.setOfficeAddress(joinSellerDto.getOfficeAddress());
        businessInfo.setBankName(joinSellerDto.getBankName());
        businessInfo.setBankAccount(joinSellerDto.getBankAccount());
        businessInfo.setDepositor(joinSellerDto.getDepositor());
        businessInfoRepository.save(businessInfo);
        return true;
    }

    @Transactional
    public Cookie[] refreshToken(Cookie[] cookies) {
        String refreshToken = null;
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("refresh_token")) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if(refreshToken == null) {
            log.error("Token Refresh Error: Cannot Found Refresh Token Cookie");
            return null;
        }
        Long userId = jwtUtil.getRefreshUserId(refreshToken);
        if(!jwtUtil.isExistRefreshToken(userId, refreshToken)) {
            log.error("Token Refresh Error: This Token Does Not Match to User's Refresh Token");
            return null;
        }
        String role = jwtUtil.getRefreshRole(refreshToken);

        Cookie[] newCookies = new Cookie[2];
        newCookies[0] = jwtUtil.createAccessToken(userId, role);
        newCookies[1] = jwtUtil.createRefreshToken(userId, role);

        // 해당 회원의 'refresh token' 컬럼 값을 업데이트. 이때 1개의 행만 업데이트한게 아니라면 예외처리 발생
        if(userRepository.updateRefreshToken(userId, newCookies[1].getValue()) != 1) {
            log.error("Token Refresh Error: Failed to Update Refresh Token");
            return null;
        }
        return newCookies;
    }


    @Transactional  // update 벌크 쿼리문을 트랜젝션 단위로 실행하기 위해 정의한 메소드: LoginFilter에서 사용하기 위해 정의
    public int setRefreshToken(Long userId, Cookie refreshToken) {
        return userRepository.updateRefreshToken(userId, refreshToken.getValue());
    }


    @Transactional // 유저 업데이트: 자신의 정보만 업데이트 가능
    public boolean updateUser(UpdateUserDto updateUserDto) {
        Long authUserId = AuthUtil.getSecurityContextUserId();
        if(authUserId == null) {
            return false;
        }
        if(!authUserId.equals(updateUserDto.getUserId())) {
            return false;
        }

        Optional<User> user = userRepository.findById(updateUserDto.getUserId());
        if(user.isEmpty()) {
            return false;
        }
        if(updateUserDto.getPassword() != null) {
            user.get().setPassword(bCryptPasswordEncoder.encode(updateUserDto.getPassword()));
        }
        if(updateUserDto.getName() != null) {
            user.get().setName(updateUserDto.getName());
        }
        if(updateUserDto.getAddress() != null) {
            user.get().setAddress(updateUserDto.getAddress());
        }
        if(updateUserDto.getBirth() != null) {
            user.get().setBirth(updateUserDto.getBirth());
        }
        if(updateUserDto.getPhone() != null) {
            user.get().setPhone(updateUserDto.getPhone());
        }
        if(updateUserDto.getZipCode() != null) {
            user.get().setZipCode(updateUserDto.getZipCode());
        }
        userRepository.save(user.get());
        return true;
    }

    // 자기 자신의 정보를 조회
    @Transactional(readOnly = true)
    public ResUserDto getMyInfo() {
        // JWT에서 회원 식별자 정보 추출
        Long userId = AuthUtil.getSecurityContextUserId();
        if(userId == null) {
            // JWT에 회원 식별자 정보가 존재하지 않는 경우, null 반환
            return null;
        }

        // 회원 식별자로 회원 조회
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            return null;
        }

        // 조회된 회원 엔티티를 ResUserDto로 변환하여 반환
        return toDto(user.get());
    }

    // 회원 식별자로 회원 정보 조회
    @Transactional(readOnly = true)
    public ResUserDto getUserInfoById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            return null;
        }
        return toDto(user.get());
    }


}
