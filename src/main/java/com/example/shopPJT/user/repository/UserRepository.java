package com.example.shopPJT.user.repository;

import com.example.shopPJT.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Query("SELECT u FROM USERS u WHERE u.email = :email AND u.isDeleted = false")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("""
        SELECT u FROM USERS u
        WHERE (u.email LIKE CONCAT('%', :email, '%')) AND u.isDeleted = false AND u.role != 'ROLE_ADMIN'
    """)
    List<User> findNotAdminByEmailKey(@Param("email") String email);

    @Query("""
        SELECT u FROM USERS u
        WHERE (u.username LIKE CONCAT('%',:username,'%')) AND u.isDeleted = false AND u.role = 'ROLE_USER'
    """)
    List<User> findUserByUsernameKey(@Param("username") String username);
    @Query("""
        SELECT u FROM USERS u
        WHERE (u.username LIKE CONCAT('%',:username,'%')) AND u.isDeleted = false AND u.role = 'ROLE_SELLER'
    """)
    List<User> findSellerByUsernameKey(@Param("username") String username);
    Optional<User> findByoAuth2Id(String oAuth2Id);


    @Modifying // 로그인 성공 시 해당 유저의 'refresh Token' 컬럼 값을 업데이트 요청
    @Query("UPDATE USERS u SET u.refreshToken = :refreshToken WHERE u.id = :userId")
    int updateRefreshToken(@Param("userId") Long userId, @Param("refreshToken") String refreshToken);

    @Modifying
    @Query("UPDATE USERS u SET u.isDeleted = true WHERE u.id = :userId")
    int setUserDeleteTrue(@Param("userId") Long userId);

}
