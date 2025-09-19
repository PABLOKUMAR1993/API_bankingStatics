package com.banking.statics.service;

import com.banking.statics.dto.CriteriaResponse;
import com.banking.statics.dto.TransactionSearchParams;
import com.banking.statics.entity.Transaction;

import java.util.List;

public interface TransactionService {
    
    Transaction updateOne(Transaction transaction);
    
    List<Transaction> createBulk(List<Transaction> transactions);
    
    CriteriaResponse getByCriteria(TransactionSearchParams searchParams);

}