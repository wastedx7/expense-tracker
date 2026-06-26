package com.danny.MoneyManagerApplication.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.danny.MoneyManagerApplication.DTO.IncomeDTO;
import com.danny.MoneyManagerApplication.entity.CategoryEntity;
import com.danny.MoneyManagerApplication.entity.IncomeEntity;
import com.danny.MoneyManagerApplication.entity.ProfileEntity;
import com.danny.MoneyManagerApplication.repository.CategoryRepository;
import com.danny.MoneyManagerApplication.repository.IncomeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncomeService {
    
    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;
    private final IncomeRepository incomeRepository;

    public IncomeDTO addIncome(IncomeDTO dto){
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findByIdAndProfileId(dto.getCategoryId(), profile.getId())
        .orElseThrow(() -> new RuntimeException("Category not found for this profile"));

        if (!"income".equalsIgnoreCase(category.getType())) {
            throw new RuntimeException("Selected category is not an income category");
        }

        IncomeEntity newIncome = toEntity(dto, profile, category);
        newIncome = incomeRepository.save(newIncome);
        return toDTO(newIncome);
    }

    public List<IncomeDTO> getCurrentMonthExpensesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate enDate = now.withDayOfMonth(now.lengthOfMonth());

        List<IncomeEntity> list =incomeRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, enDate);
        return list.stream().map(this::toDTO).toList();
    }


    public void deleteIncome(Long incomeId){
        ProfileEntity profile = profileService.getCurrentProfile();
        IncomeEntity entity = incomeRepository.findById(incomeId)
        .orElseThrow(() -> new RuntimeException("Expense not found"));

        if(entity.getProfile().getId() != profile.getId()){
            throw new RuntimeException("Unauthoized to delete this expense");
        }
        incomeRepository.delete(entity);
    }

     public List<IncomeDTO> getLatest5IncomeForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDTO).toList();
    }

    public BigDecimal getTotalIncomeForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total = incomeRepository.findTotalIncomeByProfileId(profile.getId());
        return total != null ? total : BigDecimal.ZERO;
    }

    public List<IncomeDTO> filterIncome(LocalDate startDate, LocalDate endDate, String keyword, org.springframework.data.domain.Sort sort){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate, keyword, sort);
        return list.stream().map(this::toDTO).toList();
    }


    private IncomeEntity toEntity(IncomeDTO dto, ProfileEntity profile, CategoryEntity category){
        return IncomeEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .profile(profile)
                .category(category)
                .build();
    }

    private IncomeDTO toDTO(IncomeEntity entity){
       return IncomeDTO.builder()
            .id(entity.getId())
            .name(entity.getName())
            .icon(entity.getIcon())
            .categoryId(entity.getCategory() != null ? entity.getCategory().getId(): null)
            .categoryName(entity.getCategory() != null ? entity.getCategory().getName() : "N/A")
            .amount(entity.getAmount())
            .date(entity.getDate())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
