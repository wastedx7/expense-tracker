package com.danny.MoneyManagerApplication.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.danny.MoneyManagerApplication.DTO.ExpenseDTO;
import com.danny.MoneyManagerApplication.entity.CategoryEntity;
import com.danny.MoneyManagerApplication.entity.ExpenseEntity;
import com.danny.MoneyManagerApplication.entity.ProfileEntity;
import com.danny.MoneyManagerApplication.repository.CategoryRepository;
import com.danny.MoneyManagerApplication.repository.ExpenseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class    ExpenseService {
    
    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    private final ProfileService profileService;


    public ExpenseDTO addExpense(ExpenseDTO dto){
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findByIdAndProfileId(dto.getCategoryId(), profile.getId())
        .orElseThrow(() -> new RuntimeException("Category not found for this profile"));

        if (!"expense".equalsIgnoreCase(category.getType())) {
            throw new RuntimeException("Selected category is not an expense category");
        }

        ExpenseEntity newExpense = toEntity(dto, profile, category);
        newExpense = expenseRepository.save(newExpense);
        return toDTO(newExpense);
    }

    public List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate enDate = now.withDayOfMonth(now.lengthOfMonth());

        List<ExpenseEntity> list =expenseRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, enDate);
        return list.stream().map(this::toDTO).toList();
    }

    public List<ExpenseDTO> getLatest5ExpensesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpenseEntity> list = expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDTO).toList();
    }

    public BigDecimal getTotalExpenseForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total = expenseRepository.findTotalExpenseByProfileId(profile.getId());
        return total != null ? total : BigDecimal.ZERO;
    }

    public void deleteExpense(Long expenseId){
        ProfileEntity profile = profileService.getCurrentProfile();
        ExpenseEntity entity = expenseRepository.findById(expenseId)
        .orElseThrow(() -> new RuntimeException("Expense not found"));

        if(entity.getProfile().getId() != profile.getId()){
            throw new RuntimeException("Unauthoized to delete this expense");
        }
        expenseRepository.delete(entity);
    }

    public List<ExpenseDTO> filterExpense(LocalDate startDate, LocalDate endDate, String keyword, org.springframework.data.domain.Sort sort){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate, keyword, sort);
        return list.stream().map(this::toDTO).toList();
    }

    public List<ExpenseDTO> getExpensesForUserOnDate(Long profileId, LocalDate date){
        List<ExpenseEntity> list =expenseRepository.findByProfileIdAndDate(profileId, date);
        return list.stream().map(this::toDTO).toList();
    }

    private ExpenseEntity toEntity(ExpenseDTO dto, ProfileEntity profile, CategoryEntity category){
        return ExpenseEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .profile(profile)
                .category(category)
                .build();
    }

    private ExpenseDTO toDTO(ExpenseEntity entity){
        return ExpenseDTO.builder()
            .id(entity.getId())
            .name(entity.getName())
            .icon(entity.getIcon())
            .categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
            .categoryName(entity.getCategory() != null ? entity.getCategory().getName() : "N/A")
            .amount(entity.getAmount())
            .date(entity.getDate())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

}
