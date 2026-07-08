package com.micro.expense_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.micro.expense_service.model.Income;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    Optional<Income> findByProfileEmail(String profileEmail);
}
