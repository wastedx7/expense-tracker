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
@Schema(description = "Balance summary (income, expenses, net balance)")
public class BalanceResponse {

    @Schema(description = "Total income amount", example = "5000.00")
    private BigDecimal totalIncome;

    @Schema(description = "Total expenses amount", example = "3200.00")
    private BigDecimal totalExpenses;

    @Schema(description = "Net balance (income - expenses)", example = "1800.00")
    private BigDecimal balance;
}
