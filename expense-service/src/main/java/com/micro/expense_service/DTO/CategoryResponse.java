package com.micro.expense_service.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Category response data")
public class CategoryResponse {

    @Schema(description = "Category ID", example = "1")
    private Long id;

    @Schema(description = "Category name", example = "Groceries")
    private String name;

    @Schema(description = "Category type", example = "EXPENSE")
    private String type;

    @Schema(description = "Icon identifier", example = "shopping-cart")
    private String icon;

    @Schema(description = "Hex color code", example = "#FF5733")
    private String color;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;
}
