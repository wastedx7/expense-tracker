package com.micro.expense_service.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Income set/update request")
public class IncomeRequest {

    @NotNull @Positive
    @Schema(description = "Income amount", example = "5000.00")
    private BigDecimal amount;

    @Schema(description = "Optional description", example = "Monthly salary")
    private String description;
}
