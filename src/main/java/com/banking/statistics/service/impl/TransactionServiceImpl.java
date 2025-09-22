package com.banking.statistics.service.impl;

import com.banking.statistics.dto.CriteriaResponse;
import com.banking.statistics.dto.CurrentBalanceResponse;
import com.banking.statistics.dto.TransactionSearchParams;
import com.banking.statistics.entity.Transaction;
import com.banking.statistics.entity.User;
import com.banking.statistics.repository.TransactionRepository;
import com.banking.statistics.repository.UserRepository;
import com.banking.statistics.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    
    @Override
    @Transactional
    public Transaction updateOne(Transaction transaction, String userEmail) {
        try {
            log.info("Updating transaction with ID: {} for user: {}", transaction.getId(), userEmail);
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));
            transaction.setUser(user);
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
    public List<Transaction> createBulk(List<Transaction> transactions, String userEmail) {
        try {
            log.info("Creating {} transactions in bulk for user: {}", transactions.size(), userEmail);
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));
            
            List<Transaction> nonDuplicateTransactions = transactions.stream()
                    .filter(transaction -> {
                        List<Transaction> duplicates = transactionRepository.findDuplicates(
                                userEmail,
                                transaction.getFechaOperacion(),
                                transaction.getConcepto(),
                                transaction.getPagos(),
                                transaction.getIngresos()
                        );
                        
                        if (!duplicates.isEmpty()) {
                            log.warn("Duplicate transaction found for date: {}, concept: {}, skipping", 
                                    transaction.getFechaOperacion(), transaction.getConcepto());
                            return false;
                        }
                        return true;
                    })
                    .toList();
            
            log.info("Filtered {} duplicate transactions, proceeding with {} unique transactions", 
                    transactions.size() - nonDuplicateTransactions.size(), nonDuplicateTransactions.size());
            
            nonDuplicateTransactions.forEach(transaction -> transaction.setUser(user));
            List<Transaction> createdTransactions = transactionRepository.saveAll(nonDuplicateTransactions);
            log.info("Successfully created {} transactions", createdTransactions.size());
            return createdTransactions;
        } catch (Exception e) {
            log.error("Error creating transactions in bulk: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create transactions", e);
        }
    }
    
    @Override
    public CriteriaResponse getByCriteria(TransactionSearchParams searchParams, String userEmail) {
        try {
            log.info("Searching transactions with criteria: pageNumber={}, pageSize={}, sortBy={}, sortDirection={}", 
                    searchParams.getPageNumber(), searchParams.getPageSize(), searchParams.getSortBy(), searchParams.getSortDirection());
            
            Sort.Direction direction = "asc".equalsIgnoreCase(searchParams.getSortDirection()) 
                    ? Sort.Direction.ASC 
                    : Sort.Direction.DESC;
            
            Sort sort = Sort.by(direction, searchParams.getSortBy());
            Pageable pageable = PageRequest.of(searchParams.getPageNumber(), searchParams.getPageSize(), sort);
            
            Page<Transaction> transactionPage = transactionRepository.findByCriteria(
                    userEmail,
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
    
    @Override
    public CurrentBalanceResponse getCurrentBalance(String userEmail) {
        try {
            log.info("Getting current account balance for user: {}", userEmail);
            
            Pageable pageable = PageRequest.of(0, 1);
            List<Transaction> latestTransactions = transactionRepository.findLatestTransaction(userEmail, pageable);
            
            if (latestTransactions.isEmpty()) {
                log.info("No transactions found, returning zero balance");
                return new CurrentBalanceResponse(BigDecimal.ZERO, null, false);
            }
            
            Transaction latestTransaction = latestTransactions.get(0);
            log.info("Current balance: {} from transaction date: {}", 
                    latestTransaction.getSaldo(), latestTransaction.getFechaOperacion());
            
            return new CurrentBalanceResponse(
                    latestTransaction.getSaldo(),
                    latestTransaction.getFechaOperacion(),
                    true
            );
        } catch (Exception e) {
            log.error("Error getting current balance: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get current balance", e);
        }
    }

}