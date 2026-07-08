package com.micro.expense_service.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Transaction creation/update request")
public class TransactionRequest {

    @NotNull
    @Schema(description = "Category ID", example = "1")
    private Long categoryId;

    @NotBlank
    @Schema(description = "Transaction type", example = "EXPENSE", allowableValues = {"INCOME", "EXPENSE"})
    private String type;

    @NotNull @Positive
    @Schema(description = "Transaction amount", example = "49.99")
    private BigDecimal amount;

    @Schema(description = "Optional description", example = "Weekly groceries")
    private String description;

    @NotNull
    @Schema(description = "Transaction date", example = "2026-07-08")
    private LocalDate date;
}
