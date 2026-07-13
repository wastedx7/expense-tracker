package com.micro.expense_service.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.micro.expense_service.DTO.CategoryExpenseDTO;
import com.micro.expense_service.DTO.DashboardDTO;
import com.micro.expense_service.DTO.MonthlyTrendDTO;
import com.micro.expense_service.DTO.TransactionResponse;
import com.micro.expense_service.model.Category;
import com.micro.expense_service.model.Transaction;
import com.micro.expense_service.repository.CategoryRepository;
import com.micro.expense_service.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final IncomeService incomeService;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public DashboardDTO getDashboard(String profileEmail) {
        LocalDate now = LocalDate.now();
        LocalDate monthStart = now.withDayOfMonth(1);
        LocalDate monthEnd = now.withDayOfMonth(now.lengthOfMonth());

        BigDecimal totalIncome = incomeService.getIncomeAmount(profileEmail);
        BigDecimal totalExpenses = transactionRepository.sumByProfileEmailAndType(profileEmail, "EXPENSE");
        BigDecimal balance = totalIncome.subtract(totalExpenses);

        BigDecimal currentMonthExpenses = transactionRepository
                .sumByProfileEmailAndTypeAndDateBetween(profileEmail, "EXPENSE", monthStart, monthEnd);
        BigDecimal currentMonthIncome = transactionRepository
                .sumByProfileEmailAndTypeAndDateBetween(profileEmail, "INCOME", monthStart, monthEnd);

        List<CategoryExpenseDTO> expensesByCategory = getExpensesByCategory(profileEmail, monthStart, monthEnd);

        List<TransactionResponse> recentTransactions = transactionRepository
                .findTop10ByProfileEmailOrderByDateDesc(profileEmail)
                .stream()
                .map(this::toTransactionResponse)
                .collect(Collectors.toList());

        List<MonthlyTrendDTO> monthlyTrend = getMonthlyTrend(profileEmail);

        return DashboardDTO.builder()
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .balance(balance)
                .currentMonthExpenses(currentMonthExpenses)
                .currentMonthIncome(currentMonthIncome)
                .expensesByCategory(expensesByCategory)
                .recentTransactions(recentTransactions)
                .monthlyTrend(monthlyTrend)
                .build();
    }

    private List<CategoryExpenseDTO> getExpensesByCategory(String email, LocalDate start, LocalDate end) {
        List<Object[]> results = transactionRepository.sumExpensesByCategory(email, start, end);
        List<CategoryExpenseDTO> list = new ArrayList<>();

        for (Object[] row : results) {
            Long categoryId = ((Number) row[0]).longValue();
            BigDecimal amount = (BigDecimal) row[1];
            String categoryName = categoryRepository.findById(categoryId)
                    .map(Category::getName)
                    .orElse("Unknown");

            list.add(CategoryExpenseDTO.builder()
                    .categoryId(categoryId)
                    .categoryName(categoryName)
                    .amount(amount)
                    .build());
        }

        list.sort((a, b) -> b.getAmount().compareTo(a.getAmount()));
        return list;
    }

    private List<MonthlyTrendDTO> getMonthlyTrend(String email) {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6).withDayOfMonth(1);
        List<Object[]> results = transactionRepository.getMonthlyTrend(email, sixMonthsAgo);

        Map<String, MonthlyTrendDTO> trendMap = new LinkedHashMap<>();

        for (Object[] row : results) {
            int year = ((Number) row[0]).intValue();
            int month = ((Number) row[1]).intValue();
            String type = (String) row[2];
            BigDecimal amount = (BigDecimal) row[3];

            String key = year + "-" + month;
            MonthlyTrendDTO dto = trendMap.get(key);
            if (dto == null) {
                dto = MonthlyTrendDTO.builder()
                        .year(year)
                        .month(month)
                        .expenses(BigDecimal.ZERO)
                        .income(BigDecimal.ZERO)
                        .build();
                trendMap.put(key, dto);
            }

            if ("EXPENSE".equals(type)) {
                dto.setExpenses(amount);
            } else if ("INCOME".equals(type)) {
                dto.setIncome(amount);
            }
        }

        return new ArrayList<>(trendMap.values());
    }

    private TransactionResponse toTransactionResponse(Transaction transaction) {
        String categoryName = categoryRepository.findById(transaction.getCategoryId())
                .map(Category::getName)
                .orElse("Unknown");

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
