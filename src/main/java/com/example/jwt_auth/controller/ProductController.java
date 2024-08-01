package com.example.jwt_auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwt_auth.Dto.AccessCredentialsDto;
import com.example.jwt_auth.Dto.AuthRequest;
import com.example.jwt_auth.Dto.RefreshTokenDto;
import com.example.jwt_auth.model.RefreshToken;
import com.example.jwt_auth.model.UserInfo;
import com.example.jwt_auth.service.JwtService;
import com.example.jwt_auth.service.ProductServices;
import com.example.jwt_auth.service.RefreshTokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductServices productServices;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;
    
    @GetMapping("/welcome")
    public ResponseEntity<?> welcome() {
        return new ResponseEntity<>("Welcome to our service", HttpStatus.OK);
    }
    
    @PostMapping("/new")
    public ResponseEntity<?> addNewUser(@RequestBody UserInfo newUser) {
        return new ResponseEntity<>(productServices.addUser(newUser),HttpStatus.CREATED);
    }
    
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllProducts() {
        return new ResponseEntity<>(this.productServices.getProducts(), HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getProductById(@PathVariable("id") int id) {
        return new ResponseEntity<>(this.productServices.getProductById(id), HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticationAndGetToken(@RequestBody AuthRequest authRequest){

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        if(authentication.isAuthenticated()){
            AccessCredentialsDto credentials = AccessCredentialsDto.builder()
                                                .accessToken(jwtService.generateTokenForUser(authRequest.getUsername()))
                                                .refreshToken(refreshTokenService.generateRefreshToken(authRequest.getUsername()).getRefreshToken())
                                                .build();

            return new ResponseEntity<>(credentials, HttpStatus.OK);
        }

        return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshAccessToken(@RequestBody RefreshTokenDto refreshToken){
        RefreshToken token = this.refreshTokenService.getRefreshToken(refreshToken.getToken());
        this.refreshTokenService.deleteRefreshToken(token.getUserInfo());

        AccessCredentialsDto credentials = AccessCredentialsDto.builder()
                                                .accessToken(jwtService.generateTokenForUser(token.getUserInfo().getUsername()))
                                                .refreshToken(refreshTokenService.generateRefreshToken(token.getUserInfo().getUsername()).getRefreshToken())
                                                .build();

        return new ResponseEntity<>(credentials, HttpStatus.OK);
    }
}
