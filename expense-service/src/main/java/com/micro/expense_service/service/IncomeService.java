package com.micro.expense_service.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.micro.expense_service.DTO.IncomeRequest;
import com.micro.expense_service.DTO.IncomeResponse;
import com.micro.expense_service.model.Income;
import com.micro.expense_service.repository.IncomeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incomeRepository;

    public IncomeResponse upsertIncome(String profileEmail, IncomeRequest request) {
        Income income = incomeRepository.findByProfileEmail(profileEmail)
                .orElse(Income.builder().profileEmail(profileEmail).build());

        income.setAmount(request.getAmount());
        income.setDescription(request.getDescription());

        return toResponse(incomeRepository.save(income));
    }

    public java.util.Optional<IncomeResponse> getIncome(String profileEmail) {
        return incomeRepository.findByProfileEmail(profileEmail)
                .map(this::toResponse);
    }

    public IncomeResponse addIncome(String profileEmail, BigDecimal amount) {
        Income income = incomeRepository.findByProfileEmail(profileEmail)
                .orElse(Income.builder().profileEmail(profileEmail).amount(BigDecimal.ZERO).build());

        income.setAmount(income.getAmount().add(amount));

        return toResponse(incomeRepository.save(income));
    }

    public BigDecimal getIncomeAmount(String profileEmail) {
        return incomeRepository.findByProfileEmail(profileEmail)
                .map(Income::getAmount)
                .orElse(BigDecimal.ZERO);
    }

    private IncomeResponse toResponse(Income income) {
        return IncomeResponse.builder()
                .id(income.getId())
                .amount(income.getAmount())
                .description(income.getDescription())
                .createdAt(income.getCreatedAt())
                .updatedAt(income.getUpdatedAt())
                .build();
    }
}
