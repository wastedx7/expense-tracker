package com.micro.expense_service.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.micro.expense_service.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByProfileEmail(String profileEmail);

    Optional<Transaction> findByIdAndProfileEmail(Long id, String profileEmail);

    List<Transaction> findByProfileEmailAndCategoryId(String profileEmail, Long categoryId);

    List<Transaction> findByProfileEmailAndType(String profileEmail, String type);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.profileEmail = :email AND t.type = :type")
    BigDecimal sumByProfileEmailAndType(@Param("email") String email, @Param("type") String type);
}
