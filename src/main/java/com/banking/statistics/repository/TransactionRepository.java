package com.banking.statistics.repository;

import com.banking.statistics.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    @Query("SELECT t FROM Transaction t WHERE " +
           "(:operationDateFrom IS NULL OR t.fechaOperacion >= :operationDateFrom) AND " +
           "(:operationDateTo IS NULL OR t.fechaOperacion <= :operationDateTo) AND " +
           "(:valueDateFrom IS NULL OR t.fechaValor >= :valueDateFrom) AND " +
           "(:valueDateTo IS NULL OR t.fechaValor <= :valueDateTo) AND " +
           "(:concept IS NULL OR t.concepto LIKE %:concept%) AND " +
           "(:paymentAmountFrom IS NULL OR t.pagos >= :paymentAmountFrom) AND " +
           "(:paymentAmountTo IS NULL OR t.pagos <= :paymentAmountTo) AND " +
           "(:incomeAmountFrom IS NULL OR t.ingresos >= :incomeAmountFrom) AND " +
           "(:incomeAmountTo IS NULL OR t.ingresos <= :incomeAmountTo) AND " +
           "(:categoryIds IS NULL OR t.category.id IN :categoryIds)")
    Page<Transaction> findByCriteria(
            @Param("operationDateFrom") LocalDate operationDateFrom,
            @Param("operationDateTo") LocalDate operationDateTo,
            @Param("valueDateFrom") LocalDate valueDateFrom,
            @Param("valueDateTo") LocalDate valueDateTo,
            @Param("concept") String concept,
            @Param("paymentAmountFrom") BigDecimal paymentAmountFrom,
            @Param("paymentAmountTo") BigDecimal paymentAmountTo,
            @Param("incomeAmountFrom") BigDecimal incomeAmountFrom,
            @Param("incomeAmountTo") BigDecimal incomeAmountTo,
            @Param("categoryIds") List<Long> categoryIds,
            Pageable pageable
    );

}