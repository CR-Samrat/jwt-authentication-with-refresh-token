package com.example.jwt_auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jwt_auth.model.RefreshToken;
import com.example.jwt_auth.model.UserInfo;

import java.util.Optional;
import java.util.List;



public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer>{
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    List<RefreshToken> findByUserInfo(UserInfo userInfo);
}
