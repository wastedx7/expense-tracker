package com.micro.expense_service.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Transaction response data")
public class TransactionResponse {

    @Schema(description = "Transaction ID", example = "1")
    private Long id;

    @Schema(description = "Category ID", example = "1")
    private Long categoryId;

    @Schema(description = "Category name", example = "Groceries")
    private String categoryName;

    @Schema(description = "Transaction type", example = "EXPENSE")
    private String type;

    @Schema(description = "Transaction amount", example = "49.99")
    private BigDecimal amount;

    @Schema(description = "Optional description", example = "Weekly groceries")
    private String description;

    @Schema(description = "Transaction date", example = "2026-07-08")
    private LocalDate date;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;
}
