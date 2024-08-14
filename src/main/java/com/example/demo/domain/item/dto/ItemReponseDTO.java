package com.example.demo.domain.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemReponseDTO {
    private String message;
    private String status;
}
