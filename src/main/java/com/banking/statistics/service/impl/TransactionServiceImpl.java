package com.banking.statistics.service.impl;

import com.banking.statistics.dto.CriteriaResponse;
import com.banking.statistics.dto.TransactionSearchParams;
import com.banking.statistics.entity.Transaction;
import com.banking.statistics.repository.TransactionRepository;
import com.banking.statistics.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    
    private final TransactionRepository transactionRepository;
    
    @Override
    @Transactional
    public Transaction updateOne(Transaction transaction) {
        try {
            log.info("Updating transaction with ID: {}", transaction.getId());
            Transaction updatedTransaction = transactionRepository.save(transaction);
            log.info("Successfully updated transaction with ID: {}", updatedTransaction.getId());
            return updatedTransaction;
        } catch (Exception e) {
            log.error("Error updating transaction with ID {}: {}", transaction.getId(), e.getMessage(), e);
            throw new RuntimeException("Failed to update transaction", e);
        }
    }
    
    @Override
    @Transactional
    public List<Transaction> createBulk(List<Transaction> transactions) {
        try {
            log.info("Creating {} transactions in bulk", transactions.size());
            List<Transaction> createdTransactions = transactionRepository.saveAll(transactions);
            log.info("Successfully created {} transactions", createdTransactions.size());
            return createdTransactions;
        } catch (Exception e) {
            log.error("Error creating transactions in bulk: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create transactions", e);
        }
    }
    
    @Override
    public CriteriaResponse getByCriteria(TransactionSearchParams searchParams) {
        try {
            log.info("Searching transactions with criteria: pageNumber={}, pageSize={}", 
                    searchParams.getPageNumber(), searchParams.getPageSize());
            
            Pageable pageable = PageRequest.of(searchParams.getPageNumber(), searchParams.getPageSize());
            
            Page<Transaction> transactionPage = transactionRepository.findByCriteria(
                    searchParams.getOperationDateFrom(),
                    searchParams.getOperationDateTo(),
                    searchParams.getValueDateFrom(),
                    searchParams.getValueDateTo(),
                    searchParams.getConcept(),
                    searchParams.getPaymentAmountFrom(),
                    searchParams.getPaymentAmountTo(),
                    searchParams.getIncomeAmountFrom(),
                    searchParams.getIncomeAmountTo(),
                    searchParams.getCategoryIds(),
                    pageable
            );
            
            CriteriaResponse response = new CriteriaResponse(
                    transactionPage.getTotalElements(),
                    searchParams.getPageSize(),
                    searchParams.getPageNumber(),
                    transactionPage.getContent()
            );
            
            log.info("Found {} transactions with criteria, returning page {} of {} elements", 
                    response.getTotalElementsFound(), 
                    response.getCurrentPageNumber(), 
                    response.getElements().size());
            
            return response;
        } catch (Exception e) {
            log.error("Error searching transactions by criteria: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to search transactions by criteria", e);
        }
    }

}