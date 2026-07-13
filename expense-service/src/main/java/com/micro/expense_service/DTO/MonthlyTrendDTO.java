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
@Schema(description = "Monthly expense/income summary for trend chart")
public class MonthlyTrendDTO {

    @Schema(description = "Year", example = "2026")
    private int year;

    @Schema(description = "Month (1-12)", example = "7")
    private int month;

    @Schema(description = "Total expenses for this month", example = "1200.00")
    private BigDecimal expenses;

    @Schema(description = "Total income for this month", example = "5000.00")
    private BigDecimal income;
}
