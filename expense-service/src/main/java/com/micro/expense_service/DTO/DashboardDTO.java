package com.micro.expense_service.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dashboard overview data")
public class DashboardDTO {

    @Schema(description = "Global income amount", example = "5000.00")
    private BigDecimal totalIncome;

    @Schema(description = "Total expenses (all time)", example = "3200.00")
    private BigDecimal totalExpenses;

    @Schema(description = "Net balance (income - expenses)", example = "1800.00")
    private BigDecimal balance;

    @Schema(description = "Current month expenses", example = "450.00")
    private BigDecimal currentMonthExpenses;

    @Schema(description = "Current month income from INCOME transactions", example = "200.00")
    private BigDecimal currentMonthIncome;

    @Schema(description = "Expenses broken down by category for the current month")
    private List<CategoryExpenseDTO> expensesByCategory;

    @Schema(description = "Most recent transactions")
    private List<TransactionResponse> recentTransactions;

    @Schema(description = "Monthly expense/income trend (last 6 months)")
    private List<MonthlyTrendDTO> monthlyTrend;
}
