package com.example.shopPJT.user.service;

import com.example.shopPJT.businessInfo.dto.ResBusinessInfoDto;
import com.example.shopPJT.businessInfo.entity.BusinessInfo;
import com.example.shopPJT.businessInfo.repository.BusinessInfoRepository;
import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.user.dto.*;
import com.example.shopPJT.user.entity.RoleType;
import com.example.shopPJT.user.entity.User;
import com.example.shopPJT.user.repository.UserRepository;
import com.example.shopPJT.util.AuthUtil;
import com.example.shopPJT.util.JwtUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
    private final BusinessInfoRepository businessInfoRepository;
    @PersistenceContext
    private EntityManager entityManager;

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
        resUserDto.setUsername(user.getUsername());
        resUserDto.setName(user.getName());
        resUserDto.setEmail(user.getEmail());
        resUserDto.setPhone(user.getPhone());
        if(user.getBirth() != null) { resUserDto.setBirth(user.getBirth().toString()); }
        resUserDto.setAddress(user.getAddress());
        resUserDto.setZipCode(user.getZipCode());
        resUserDto.setRole(user.getRole().toString());
        return resUserDto;
    }


    // 이메일 중복 검사
    @Transactional(readOnly = true)
    public void isExistEmail(String email) {
        if(userRepository.existsByEmail(email)) {
            throw new ApplicationException(ApplicationError.DUPLICATE_EMAIL);
        }
    }

    // 유저네임 중복 검사
    @Transactional(readOnly = true)
    public void isExistUsername(String username) {
        if(userRepository.existsByUsername(username)) {
            throw new ApplicationException(ApplicationError.DUPLICATE_USERNAME);
        }
    }

    // 회원가입 공통로직
    private User basicJoin(JoinDto joinDto) {
        // 이메일, 비밀번호, 이름 중 하나라도 값을 전달받지 못하면 null 반환
        if(joinDto.getEmail() == null || joinDto.getPassword() == null
                || joinDto.getName() == null) {
            return null;
        }
        User user = new User();
        user.setEmail(joinDto.getEmail());
        // 비밀번호는 암호화하여 DB에 저장
        String encodedPassword = bCryptPasswordEncoder.encode(joinDto.getPassword());
        user.setPassword(encodedPassword);
        user.setName(joinDto.getName());
        user.setAddress(joinDto.getAddress());
        user.setBirth(joinDto.getBirth());
        user.setZipCode(joinDto.getZipCode());
        return user;
    }

    // 기본 회원가입: 일반 사용자
    @Transactional
    public boolean join(JoinUserDto joinUserDto) {
        User user = basicJoin(joinUserDto);
        if(user == null){
            throw new ApplicationException(ApplicationError.USER_NOT_FOUND);
        }
        if(userRepository.existsByUsername(joinUserDto.getUsername())) {
            // 일반 사용자는 유저네임 중복 불가능
            throw new ApplicationException(ApplicationError.DUPLICATE_USERNAME);
        }
        user.setUsername(joinUserDto.getUsername());
        userRepository.save(user);
        return true;
    }

    // 기본 회원가입: 판매자 유형
    @Transactional
    public boolean joinSeller(JoinSellerDto joinSellerDto) {
        User user = basicJoin(joinSellerDto);
        if(user == null){
            throw new ApplicationException(ApplicationError.USER_NOT_FOUND);
        }
        user.setUsername(joinSellerDto.getBusinessName()); // 회원 유저네임을 '사업자명'으로 변경.
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
        businessInfo.setBusinessName(joinSellerDto.getBusinessName());
        businessInfoRepository.save(businessInfo);
        return true;
    }

    @Transactional
    public void completeJoin(Long userId, JoinCompleteDto joinCompleteDto) {
        if(userId == null) {
            throw new ApplicationException(ApplicationError.USERID_NOT_FOUND);
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new ApplicationException(ApplicationError.USER_NOT_FOUND));
        user.setUsername(joinCompleteDto.getUsername());
        user.setBirth(joinCompleteDto.getBirth());
        user.setPhone(joinCompleteDto.getPhone());
        user.setAddress(joinCompleteDto.getAddress());
        user.setZipCode(joinCompleteDto.getZipCode());
        userRepository.save(user);
    }

    @Transactional
    public ResponseCookie[] refreshToken(Cookie[] cookies) {
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
            throw new ApplicationException(ApplicationError.REFRESH_TOKEN_NOT_FOUND);
        }
        Long userId = jwtUtil.getRefreshUserId(refreshToken);
        if(!jwtUtil.isExistRefreshToken(userId, refreshToken)) {
            log.error("Token Refresh Error: This Token Does Not Match to User's Refresh Token");
            throw new ApplicationException(ApplicationError.REFRESH_TOKEN_NOT_MATCH);
        }
        String role = jwtUtil.getRefreshRole(refreshToken);

        ResponseCookie[] newCookies = new ResponseCookie[2];
        newCookies[0] = jwtUtil.createAccessToken(userId, role);
        newCookies[1] = jwtUtil.createRefreshToken(userId, role);

        // 해당 회원의 'refresh token' 컬럼 값을 업데이트. 이때 1개의 행만 업데이트한게 아니라면 예외처리 발생
        if(userRepository.updateRefreshToken(userId, newCookies[1].getValue()) != 1) {
            log.error("Token Refresh Error: Failed to Update Refresh Token");
            throw new ApplicationException(ApplicationError.REFRESH_TOKEN_UPDATE_ERROR);
        }
        return newCookies;
    }


    @Transactional  // update 벌크 쿼리문을 트랜젝션 단위로 실행하기 위해 정의한 메소드: LoginFilter에서 사용하기 위해 정의
    public int setRefreshToken(Long userId, ResponseCookie refreshToken) {
        int result = userRepository.updateRefreshToken(userId, refreshToken.getValue());
        // 벌크 연산 후 영속성 컨텍스트 비우기: 이 트랙젝션 범위에만 국지적으로 영향을 미친다.
        entityManager.flush();
        entityManager.clear();
        return result;
    }


    @Transactional // 유저 업데이트: 자신의 정보만 업데이트 가능
    public boolean updateUser(UpdateUserDto updateUserDto) {
        Long authUserId = AuthUtil.getSecurityContextUserId();
        String authUserRole = AuthUtil.getCurrentUserAuthority();
        if(authUserId == null) {
            throw new ApplicationException(ApplicationError.USERID_NOT_FOUND);
        }
        if(!authUserId.equals(updateUserDto.getUserId())) {
            throw new ApplicationException(ApplicationError.ACCESS_NOT_ALLOWED);
        }
        if(authUserRole == null) {
            throw new ApplicationException(ApplicationError.ACCESS_NOT_ALLOWED);
        }

        Optional<User> user = userRepository.findById(updateUserDto.getUserId());
        if(user.isEmpty()) {
            throw new ApplicationException(ApplicationError.USER_NOT_FOUND);
        }
        if(updateUserDto.getPassword() != null) {
            user.get().setPassword(bCryptPasswordEncoder.encode(updateUserDto.getPassword()));
        }
        if(updateUserDto.getName() != null && !authUserRole.equals("SELLER")) {
            // 판매자는 사업자명이 변경되어야만 유저 이름 값이 변경된다.
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
        // 트랜잭션으로 관리되는 메소드는 메소드 종료 시 '트랜잭션 커밋'이 발생하므로, 별도의 save() 호출이 필요없다.(변경사항 자동 커밋)
//        userRepository.save(user.get());
        return true;
    }

    // 자기 자신의 정보를 조회
    @Transactional(readOnly = true)
    public ResUserDto getMyInfo() {
        // JWT에서 회원 식별자 정보 추출
        Long userId = AuthUtil.getSecurityContextUserId();
        if(userId == null) {
            // JWT에 회원 식별자 정보가 존재하지 않는 경우, null 반환
            throw new ApplicationException(ApplicationError.USERID_NOT_FOUND);
        }

        // 회원 식별자로 회원 조회
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            throw new ApplicationException(ApplicationError.USER_NOT_FOUND);
        }

        // 조회된 회원 엔티티를 ResUserDto로 변환하여 반환
        return toDto(user.get());
    }

    // 회원 식별자로 회원 정보 조회
    @Transactional(readOnly = true)
    public ResUserDto getUserInfoById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            throw new ApplicationException(ApplicationError.USER_NOT_FOUND);
        }

        if(user.get().getIsDeleted()) {
            // '삭제처리'된 회원의 정보는 제공하지 않는다.
            throw new ApplicationException(ApplicationError.USER_DELETED);
        }

        return toDto(user.get());
    }

    @Transactional(readOnly = true)
    public List<ResUserDto> getUserInfoByEmail(String email) {
        List<User> userList = userRepository.findNotAdminByEmailKey(email);
        if(userList.isEmpty()) {
            throw new ApplicationException(ApplicationError.USER_NOT_FOUND);
        }
        return userList.stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true) // 유저네임 키워드로 일반 회원 조회
    public List<ResUserDto> getUserListByUsernameKey(String usernameKey) {
        List<User> userList = userRepository.findUserByUsernameKey(usernameKey);
        if(userList.isEmpty()) {
            throw new ApplicationException(ApplicationError.USER_NOT_FOUND);
        }
        return userList.stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<ResUserDto> getSellerListByUsernameKey(String usernameKey) {
        List<User> userList = userRepository.findSellerByUsernameKey(usernameKey);
        if(userList.isEmpty()) {
            throw new ApplicationException(ApplicationError.USER_NOT_FOUND);
        }
        return userList.stream().map(this::toDto).toList();
    }

    @Transactional // 자기 자신 삭제(탈퇴) 처리
    public boolean userDeleteTrue(Long userId) {
        if(userId == null) {
            // JWT에 회원 식별자 정보가 존재하지 않는 경우, null 반환
            throw new ApplicationException(ApplicationError.USERID_NOT_FOUND);
        }

        int affectedUser = userRepository.setUserDeleteTrue(userId);
        // 벌크연산 후 영속성 컨텍스트 동기화(초기화)
        entityManager.flush();
        entityManager.clear();

        // '삭제처리' 쿼리에 영향을 받은 행이 1개가 아니라면 실패: 예외처리
        if(affectedUser != 1) {
            log.error("User Delete Error");
            throw new ApplicationException(ApplicationError.USER_DELETE_FAIL);
        } else {
            return true;
        }
    }


    @Transactional // 특정 유저의 권한을 '판매자'로 변경
    public boolean grantSellerAuth(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            throw new ApplicationException(ApplicationError.USER_NOT_FOUND);
        }

        user.get().setRole(RoleType.ROLE_SELLER);
        userRepository.save(user.get());
        return true;
    }

    @Transactional // 판매자 권한 회수
    public boolean revokeSellerAuth(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            throw new ApplicationException(ApplicationError.USER_NOT_FOUND);
        }

        user.get().setRole(RoleType.ROLE_USER);
        userRepository.save(user.get());
        return true;
    }

}