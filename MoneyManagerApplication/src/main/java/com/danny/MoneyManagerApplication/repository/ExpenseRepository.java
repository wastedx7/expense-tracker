package com.danny.MoneyManagerApplication.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.danny.MoneyManagerApplication.entity.ExpenseEntity;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity,Long> {
    List<ExpenseEntity> findByProfileIdOrderByDateDesc(Long profileId);

    List<ExpenseEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);

    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.profile.id = :profileId")
    BigDecimal findTotalExpenseByProfileId(@Param("profileId") Long profileId);

    List<ExpenseEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
        Long profileId,
        LocalDate startDate,
        LocalDate ensDate,
        String keyword,
        org.springframework.data.domain.Sort sort            
    );

    List<ExpenseEntity> findByProfileIdAndDate(Long profileId, LocalDate date);

    List<ExpenseEntity> findByProfileIdAndDateBetween(Long profilrId, LocalDate startDate, LocalDate endDate);
    
}
