package com.micro.expense_service.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.micro.expense_service.DTO.BalanceResponse;
import com.micro.expense_service.DTO.TransactionRequest;
import com.micro.expense_service.DTO.TransactionResponse;
import com.micro.expense_service.model.Category;
import com.micro.expense_service.model.Transaction;
import com.micro.expense_service.repository.CategoryRepository;
import com.micro.expense_service.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final IncomeService incomeService;

    public TransactionResponse createTransaction(String profileEmail, TransactionRequest request) {
        Category category = categoryRepository.findByIdAndProfileEmail(request.getCategoryId(), profileEmail)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        if (!request.getType().equals(category.getType())) {
            throw new IllegalArgumentException("Transaction type must match category type");
        }

        Transaction transaction = Transaction.builder()
                .profileEmail(profileEmail)
                .categoryId(request.getCategoryId())
                .type(request.getType())
                .amount(request.getAmount())
                .description(request.getDescription())
                .date(request.getDate())
                .build();

        return toResponse(transactionRepository.save(transaction), category.getName());
    }

    public List<TransactionResponse> getTransactions(String profileEmail, Long categoryId, String type) {
        List<Transaction> transactions;

        if (categoryId != null) {
            transactions = transactionRepository.findByProfileEmailAndCategoryId(profileEmail, categoryId);
        } else if (type != null) {
            transactions = transactionRepository.findByProfileEmailAndType(profileEmail, type);
        } else {
            transactions = transactionRepository.findByProfileEmail(profileEmail);
        }

        return transactions.stream()
                .map(tx -> {
                    String categoryName = categoryRepository.findById(tx.getCategoryId())
                            .map(Category::getName)
                            .orElse("Unknown");
                    return toResponse(tx, categoryName);
                })
                .collect(Collectors.toList());
    }

    public TransactionResponse getTransaction(String profileEmail, Long id) {
        Transaction transaction = transactionRepository.findByIdAndProfileEmail(id, profileEmail)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        String categoryName = categoryRepository.findById(transaction.getCategoryId())
                .map(Category::getName)
                .orElse("Unknown");

        return toResponse(transaction, categoryName);
    }

    public TransactionResponse updateTransaction(String profileEmail, Long id, TransactionRequest request) {
        Transaction transaction = transactionRepository.findByIdAndProfileEmail(id, profileEmail)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        Category category = categoryRepository.findByIdAndProfileEmail(request.getCategoryId(), profileEmail)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        if (!request.getType().equals(category.getType())) {
            throw new IllegalArgumentException("Transaction type must match category type");
        }

        return doUpdate(transaction, category, request);
    }

    private TransactionResponse doUpdate(Transaction transaction, Category category, TransactionRequest request) {
        transaction.setCategoryId(request.getCategoryId());
        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setDate(request.getDate());

        return toResponse(transactionRepository.save(transaction), category.getName());
    }

    public void deleteTransaction(String profileEmail, Long id) {
        Transaction transaction = transactionRepository.findByIdAndProfileEmail(id, profileEmail)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        transactionRepository.delete(transaction);
    }

    public BalanceResponse getBalance(String profileEmail) {
        BigDecimal totalIncome = incomeService.getIncomeAmount(profileEmail);
        BigDecimal totalExpenses = transactionRepository.sumByProfileEmailAndType(profileEmail, "EXPENSE");
        BigDecimal balance = totalIncome.subtract(totalExpenses);

        return BalanceResponse.builder()
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .balance(balance)
                .build();
    }

    private TransactionResponse toResponse(Transaction transaction, String categoryName) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .categoryId(transaction.getCategoryId())
                .categoryName(categoryName)
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .date(transaction.getDate())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
