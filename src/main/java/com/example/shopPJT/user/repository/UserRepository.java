package com.example.shopPJT.user.repository;

import com.example.shopPJT.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u.email FROM USERS u where u.email = :email")
    Optional<User> findEmailByEmail(@Param("email") String email);

    Optional<User> findByEmail(String email);
    Optional<User> findByoAuth2Id(String oAuth2Id);


    @Modifying // 로그인 성공 시 해당 유저의 'refresh Token' 컬럼 값을 업데이트 요청
    @Query("UPDATE USERS u SET u.refreshToken = :refreshToken WHERE u.id = :userId")
    int updateRefreshToken(@Param("userId") Long userId, @Param("refreshToken") String refreshToken);

    @Modifying
    @Query("UPDATE USERS u SET u.isDeleted = true WHERE u.id = :userId")
    int setUserDeleteTrue(@Param("userId") Long userId);

}
