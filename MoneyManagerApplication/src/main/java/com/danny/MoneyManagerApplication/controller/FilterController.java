package com.danny.MoneyManagerApplication.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danny.MoneyManagerApplication.DTO.ExpenseDTO;
import com.danny.MoneyManagerApplication.DTO.FilterDTO;
import com.danny.MoneyManagerApplication.DTO.IncomeDTO;
import com.danny.MoneyManagerApplication.service.ExpenseService;
import com.danny.MoneyManagerApplication.service.IncomeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/filter")
@RequiredArgsConstructor
public class FilterController {
    
    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<?> filterTraansaction(@RequestBody FilterDTO filter){

        LocalDate startDate = filter.getStartDate() != null ? filter.getStartDate() : LocalDate.MIN;
        LocalDate endDate = filter.getEndDate() != null ? filter.getEndDate() : LocalDate.now();
        String keyword = filter.getKeyword() != null ? filter.getKeyword() : "";
        String sortField = filter.getSortField() != null ? filter.getSortField() : "date";
        String sortOrder = filter.getSortOrder() != null ? filter.getSortOrder() : "asc";
        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, sortField);

        if("income".equals(filter.getType())){
            List<IncomeDTO> income =incomeService.filterIncome(startDate, endDate, keyword, sort);
            return ResponseEntity.ok(income);
        }else if("expense".equalsIgnoreCase(filter.getType())){
            List<ExpenseDTO> expense = expenseService.filterExpense(startDate, endDate, keyword, sort);
            return ResponseEntity.ok(expense);
        }else{
            return ResponseEntity.badRequest().body("Invalid type must be 'income' or 'expense'");
        }
    }
}
