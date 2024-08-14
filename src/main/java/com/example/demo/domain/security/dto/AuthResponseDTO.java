package com.example.demo.domain.security.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDTO {
        private String accessToken;
        private String message;
        private String status;

}
