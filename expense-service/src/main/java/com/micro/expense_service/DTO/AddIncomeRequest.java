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
@Schema(description = "Request to add an amount to existing income")
public class AddIncomeRequest {

    @NotNull @Positive
    @Schema(description = "Amount to add to current income", example = "500.00")
    private BigDecimal amount;
}
