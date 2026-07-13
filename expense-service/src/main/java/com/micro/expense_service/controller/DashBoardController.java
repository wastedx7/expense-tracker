package com.micro.expense_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.micro.expense_service.DTO.DashboardDTO;
import com.micro.expense_service.service.DashboardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/expense/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard overview API (summary, trends, breakdowns)")
public class DashBoardController {

    private final DashboardService dashboardService;

    @Operation(summary = "Get dashboard", description = "Returns a comprehensive dashboard overview including income, expenses, balance, category breakdown, recent transactions, and monthly trend")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dashboard data retrieved",
            content = @Content(schema = @Schema(implementation = DashboardDTO.class)))
    })
    @GetMapping
    public ResponseEntity<DashboardDTO> displayDashboard(Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(dashboardService.getDashboard(email));
    }
}
