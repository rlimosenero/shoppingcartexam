package com.example.demo.domain.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenResponse {

    private String token ;
    private String refreshToken;
}
