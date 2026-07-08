package com.micro.expense_service.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.micro.expense_service.DTO.BalanceResponse;
import com.micro.expense_service.DTO.CategoryRequest;
import com.micro.expense_service.DTO.CategoryResponse;
import com.micro.expense_service.DTO.TransactionRequest;
import com.micro.expense_service.DTO.TransactionResponse;
import com.micro.expense_service.service.CategoryService;
import com.micro.expense_service.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/expense")
@RequiredArgsConstructor
@Tag(name = "Expense", description = "Expense management API (categories, transactions, balance)")
public class ExpenseController {

    private final CategoryService categoryService;
    private final TransactionService transactionService;

    @Operation(summary = "Health check", description = "Simple endpoint to verify the service is running")
    @GetMapping("/test")
    public String test() {
        return "Expense service is running";
    }

    @Operation(summary = "Create a category", description = "Creates a new expense/income category for the authenticated user")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Category created",
            content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/categories")
    public ResponseEntity<CategoryResponse> createCategory(
            Authentication auth,
            @Valid @RequestBody CategoryRequest request) {
        String email = auth.getName();
        CategoryResponse response = categoryService.createCategory(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "List categories", description = "Returns all categories for the authenticated user")
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getCategories(Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(categoryService.getCategories(email));
    }

    @Operation(summary = "Get a category", description = "Returns a single category by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category found"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryResponse> getCategory(Authentication auth, @PathVariable Long id) {
        String email = auth.getName();
        return ResponseEntity.ok(categoryService.getCategory(email, id));
    }

    @Operation(summary = "Update a category", description = "Updates an existing category")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category updated"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            Authentication auth,
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        String email = auth.getName();
        return ResponseEntity.ok(categoryService.updateCategory(email, id, request));
    }

    @Operation(summary = "Delete a category", description = "Deletes a category by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Category deleted"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(Authentication auth, @PathVariable Long id) {
        String email = auth.getName();
        categoryService.deleteCategory(email, id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create a transaction", description = "Creates a new income/expense transaction")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Transaction created",
            content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/transactions")
    public ResponseEntity<TransactionResponse> createTransaction(
            Authentication auth,
            @Valid @RequestBody TransactionRequest request) {
        String email = auth.getName();
        TransactionResponse response = transactionService.createTransaction(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "List transactions", description = "Returns transactions for the authenticated user, optionally filtered by category or type")
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            Authentication auth,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String type) {
        String email = auth.getName();
        return ResponseEntity.ok(transactionService.getTransactions(email, categoryId, type));
    }

    @Operation(summary = "Get a transaction", description = "Returns a single transaction by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transaction found"),
        @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @GetMapping("/transactions/{id}")
    public ResponseEntity<TransactionResponse> getTransaction(Authentication auth, @PathVariable Long id) {
        String email = auth.getName();
        return ResponseEntity.ok(transactionService.getTransaction(email, id));
    }

    @Operation(summary = "Update a transaction", description = "Updates an existing transaction")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transaction updated"),
        @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @PutMapping("/transactions/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            Authentication auth,
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request) {
        String email = auth.getName();
        return ResponseEntity.ok(transactionService.updateTransaction(email, id, request));
    }

    @Operation(summary = "Delete a transaction", description = "Deletes a transaction by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Transaction deleted"),
        @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<Void> deleteTransaction(Authentication auth, @PathVariable Long id) {
        String email = auth.getName();
        transactionService.deleteTransaction(email, id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get balance", description = "Returns the total income, expenses, and net balance for the authenticated user")
    @GetMapping("/balance")
    public ResponseEntity<BalanceResponse> getBalance(Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(transactionService.getBalance(email));
    }
}
