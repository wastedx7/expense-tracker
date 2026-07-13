package com.micro.expense_service.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.profileEmail = :email AND t.type = :type AND t.date BETWEEN :start AND :end")
    BigDecimal sumByProfileEmailAndTypeAndDateBetween(@Param("email") String email, @Param("type") String type, @Param("start") LocalDate start, @Param("end") LocalDate end);

    List<Transaction> findTop10ByProfileEmailOrderByDateDesc(String profileEmail);

    @Query("SELECT t.categoryId, COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.profileEmail = :email AND t.type = 'EXPENSE' AND t.date BETWEEN :start AND :end GROUP BY t.categoryId")
    List<Object[]> sumExpensesByCategory(@Param("email") String email, @Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT YEAR(t.date), MONTH(t.date), t.type, COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.profileEmail = :email AND t.date >= :since GROUP BY YEAR(t.date), MONTH(t.date), t.type ORDER BY YEAR(t.date), MONTH(t.date)")
    List<Object[]> getMonthlyTrend(@Param("email") String email, @Param("since") LocalDate since);

}
