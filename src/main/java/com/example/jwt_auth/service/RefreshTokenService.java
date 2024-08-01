package com.example.jwt_auth.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.stereotype.Service;

import com.example.jwt_auth.model.RefreshToken;
import com.example.jwt_auth.model.UserInfo;
import com.example.jwt_auth.repository.RefreshTokenRepository;
import com.example.jwt_auth.repository.UserInfoRepository;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    public RefreshToken generateRefreshToken(String username){

        this.refreshTokenRepository.deleteAll(this.refreshTokenRepository.findByUserInfo(this.userInfoRepository.findByUsername(username).get()));

        RefreshToken refreshToken = RefreshToken.builder()
                                                .userInfo(this.userInfoRepository.findByUsername(username).get())
                                                .refreshToken(UUID.randomUUID().toString())
                                                .expiryDate(Instant.now().plusMillis(1000*60*10))
                                                .build();
        
        return this.refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken getRefreshToken(String refreshToken){
        Optional<RefreshToken> tokenDetails = this.refreshTokenRepository.findByRefreshToken(refreshToken);

        if(tokenDetails.isPresent()){
            return tokenDetails.get();
        }

        throw new RuntimeException("Invalid token");
    }

    public void deleteRefreshToken(UserInfo user){
        List<RefreshToken> byUserInfo = this.refreshTokenRepository.findByUserInfo(user);

        if(byUserInfo.size()>0){
            this.refreshTokenRepository.deleteAll(byUserInfo);
        }
    }
}
