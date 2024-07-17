package com.example.jwt_auth.service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.jwt_auth.Dto.Product;
import com.example.jwt_auth.model.UserInfo;
import com.example.jwt_auth.repository.UserInfoRepository;

import jakarta.annotation.PostConstruct;

@Service
public class ProductServices {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    List<Product> productList = null;

    @PostConstruct
    public void loadProductFromDB(){
        productList = IntStream.rangeClosed(1, 100)
                        .mapToObj(i -> Product.builder()
                        .product_id(i)
                        .name("Product "+i)
                        .qty(new Random().nextInt(10))
                        .price(new Random().nextInt(5000)).build()
                        ).collect(Collectors.toList());
    }

    public List<Product> getProducts(){
        return productList;
    }

    public Product getProductById(int id){
        return productList.stream()
                            .filter(p -> p.getProduct_id() == id)
                            .findAny()
                            .orElseThrow(() -> new RuntimeException("product "+id+" not found"));
    }

    public String addUser(UserInfo userInfo){
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));

        this.userInfoRepository.save(userInfo);

        return "User added to system";
    }
}
