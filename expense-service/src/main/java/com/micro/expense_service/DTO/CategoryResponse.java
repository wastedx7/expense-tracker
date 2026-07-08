package com.micro.expense_service.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CategoryResponse {

    private Long id;
    private String name;
    private String type;
    private String icon;
    private String color;
    private LocalDateTime createdAt;
}
