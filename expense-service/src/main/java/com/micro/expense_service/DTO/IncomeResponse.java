package com.micro.expense_service.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Income response data")
public class IncomeResponse {

    @Schema(description = "Income record ID", example = "1")
    private Long id;

    @Schema(description = "Income amount", example = "5000.00")
    private BigDecimal amount;

    @Schema(description = "Optional description", example = "Monthly salary")
    private String description;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}
