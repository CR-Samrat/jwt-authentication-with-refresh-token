package com.example.jwt_auth.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessCredentialsDto {
    private String accessToken;
    private String refreshToken;
}
