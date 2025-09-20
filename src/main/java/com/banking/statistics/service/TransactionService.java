package com.banking.statistics.service;

import com.banking.statistics.dto.CriteriaResponse;
import com.banking.statistics.dto.CurrentBalanceResponse;
import com.banking.statistics.dto.TransactionSearchParams;
import com.banking.statistics.entity.Transaction;

import java.util.List;

public interface TransactionService {
    
    Transaction updateOne(Transaction transaction);
    
    List<Transaction> createBulk(List<Transaction> transactions);
    
    CriteriaResponse getByCriteria(TransactionSearchParams searchParams);
    
    CurrentBalanceResponse getCurrentBalance();

}