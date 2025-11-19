package com.banking.statistics.service;

import com.banking.statistics.dto.ChartDataResponse;
import com.banking.statistics.dto.CriteriaResponse;
import com.banking.statistics.dto.CurrentBalanceResponse;
import com.banking.statistics.dto.TransactionSearchParams;
import com.banking.statistics.entity.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    
    Transaction updateOne(Transaction transaction, String userEmail);
    
    List<Transaction> createBulk(List<Transaction> transactions, String userEmail);
    
    CriteriaResponse getByCriteria(TransactionSearchParams searchParams, String userEmail);
    
    CurrentBalanceResponse getCurrentBalance(String userEmail);
    
    ChartDataResponse getChartData(LocalDate dateFrom, LocalDate dateTo, String userEmail);

}