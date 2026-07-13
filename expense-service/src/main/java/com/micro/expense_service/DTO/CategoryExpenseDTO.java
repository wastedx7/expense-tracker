package com.micro.expense_service.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Expense amount grouped by category")
public class CategoryExpenseDTO {

    @Schema(description = "Category ID")
    private Long categoryId;

    @Schema(description = "Category name", example = "Groceries")
    private String categoryName;

    @Schema(description = "Total expense amount for this category", example = "250.00")
    private BigDecimal amount;
}
