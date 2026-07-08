package com.micro.expense_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.micro.expense_service.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByProfileEmail(String profileEmail);

    Optional<Category> findByIdAndProfileEmail(Long id, String profileEmail);

    boolean existsByProfileEmailAndName(String profileEmail, String name);
}
