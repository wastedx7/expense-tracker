package com.micro.expense_service.controller;

import java.util.List;

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
public class ExpenseController {

    private final CategoryService categoryService;
    private final TransactionService transactionService;

    @GetMapping("/test")
    public String test() {
        return "Expense service is running";
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryResponse> createCategory(
            Authentication auth,
            @Valid @RequestBody CategoryRequest request) {
        String email = auth.getName();
        CategoryResponse response = categoryService.createCategory(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getCategories(Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(categoryService.getCategories(email));
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryResponse> getCategory(Authentication auth, @PathVariable Long id) {
        String email = auth.getName();
        return ResponseEntity.ok(categoryService.getCategory(email, id));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            Authentication auth,
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        String email = auth.getName();
        return ResponseEntity.ok(categoryService.updateCategory(email, id, request));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(Authentication auth, @PathVariable Long id) {
        String email = auth.getName();
        categoryService.deleteCategory(email, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transactions")
    public ResponseEntity<TransactionResponse> createTransaction(
            Authentication auth,
            @Valid @RequestBody TransactionRequest request) {
        String email = auth.getName();
        TransactionResponse response = transactionService.createTransaction(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            Authentication auth,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String type) {
        String email = auth.getName();
        return ResponseEntity.ok(transactionService.getTransactions(email, categoryId, type));
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<TransactionResponse> getTransaction(Authentication auth, @PathVariable Long id) {
        String email = auth.getName();
        return ResponseEntity.ok(transactionService.getTransaction(email, id));
    }

    @PutMapping("/transactions/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            Authentication auth,
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request) {
        String email = auth.getName();
        return ResponseEntity.ok(transactionService.updateTransaction(email, id, request));
    }

    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<Void> deleteTransaction(Authentication auth, @PathVariable Long id) {
        String email = auth.getName();
        transactionService.deleteTransaction(email, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/balance")
    public ResponseEntity<BalanceResponse> getBalance(Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(transactionService.getBalance(email));
    }
}
