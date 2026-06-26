package com.danny.MoneyManagerApplication.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.danny.MoneyManagerApplication.DTO.ExpenseDTO;
import com.danny.MoneyManagerApplication.DTO.IncomeDTO;
import com.danny.MoneyManagerApplication.DTO.RecentTransactionDTO;
import com.danny.MoneyManagerApplication.entity.ProfileEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {
    
    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final ProfileService profileService;

    public Map<String , Object> getDashboardData(){
        ProfileEntity profile = profileService.getCurrentProfile();
        Map<String, Object> returnValue = new HashMap<>();
        
        List<IncomeDTO> latestIncome = incomeService.getLatest5IncomeForCurrentUser();
        List<ExpenseDTO> latestExpense = expenseService.getLatest5ExpensesForCurrentUser();

        List<RecentTransactionDTO> recentTransaction = Stream.concat(
                latestIncome.stream().map(income -> 
                    RecentTransactionDTO.builder()
                        .id(income.getId())
                        .profileId(profile.getId())
                        .icon(income.getIcon())
                        .name(income.getName())
                        .amount(income.getAmount())
                        .date(income.getDate())
                        .createdAt(income.getCreatedAt())
                        .updatedAt(income.getUpdatedAt())
                        .type("income")
                        .build()
                ),
                latestExpense.stream().map( expense -> 
                    RecentTransactionDTO.builder()
                        .id(expense.getId())
                        .profileId(profile.getId())
                        .icon(expense.getIcon())
                        .name(expense.getName())
                        .amount(expense.getAmount())
                        .date(expense.getDate())
                        .createdAt(expense.getCreatedAt())
                        .updatedAt(expense.getUpdatedAt())
                        .type("expense")
                        .build()
                )
        )
        .sorted((a, b) -> {
            int cmp = b.getDate().compareTo(a.getDate());
            if (cmp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null) {
                return b.getCreatedAt().compareTo(a.getCreatedAt());
            }
            return cmp;
        })
        .collect(Collectors.toList());

        returnValue.put("totalBalance",
               incomeService.getTotalIncomeForCurrentUser()
                .subtract(expenseService.getTotalExpenseForCurrentUser()));

        returnValue.put("totalIncome", incomeService.getTotalIncomeForCurrentUser());
        returnValue.put("totalExpense", expenseService.getTotalExpenseForCurrentUser());
        returnValue.put("recent5Expenses", latestExpense);
        returnValue.put("recent5Income", latestIncome);
        returnValue.put("recentTransactions", recentTransaction);

        return returnValue;
    }

}
