package com.micro.expense_service.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Category creation/update request")
public class CategoryRequest {

    @NotBlank
    @Schema(description = "Category name", example = "Groceries")
    private String name;

    @NotBlank
    @Schema(description = "Category type", example = "EXPENSE", allowableValues = {"INCOME", "EXPENSE"})
    private String type;

    @Schema(description = "Icon identifier", example = "shopping-cart")
    private String icon;

    @Schema(description = "Hex color code", example = "#FF5733")
    private String color;
}
