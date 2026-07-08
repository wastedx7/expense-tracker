package com.micro.expense_service.DTO;

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
public class TransactionRequest {

    @NotNull
    private Long categoryId;

    @NotBlank
    private String type;

    @NotNull @Positive
    private BigDecimal amount;

    private String description;

    @NotNull
    private LocalDate date;
}
