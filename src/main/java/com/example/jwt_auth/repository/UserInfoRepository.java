package com.example.jwt_auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jwt_auth.model.UserInfo;
import java.util.Optional;


public interface UserInfoRepository extends JpaRepository<UserInfo, Long>{
    Optional<UserInfo> findByUsername(String username);
}
