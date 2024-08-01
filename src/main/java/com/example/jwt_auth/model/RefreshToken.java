package com.example.jwt_auth.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refresh_token")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name="expiry_date")
    private Instant expiryDate;

    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private UserInfo userInfo;
}
