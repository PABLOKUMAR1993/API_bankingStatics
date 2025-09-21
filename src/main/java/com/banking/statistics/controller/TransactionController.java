package com.banking.statistics.controller;

import com.banking.statistics.constant.ApiConstants;
import com.banking.statistics.dto.CriteriaResponse;
import com.banking.statistics.dto.CurrentBalanceResponse;
import com.banking.statistics.dto.TransactionSearchParams;
import com.banking.statistics.entity.Transaction;
import com.banking.statistics.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.TRANSACTIONS_BASE_PATH)
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PutMapping(ApiConstants.TRANSACTIONS_UPDATE)
    public ResponseEntity<Transaction> updateOne(@PathVariable Long id, @RequestBody Transaction transaction) {
        String userEmail = getCurrentUserEmail();
        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            if (transaction == null) {
                logger.warn("Received null transaction for update with id: {}", id);
                return ResponseEntity.badRequest().build();
            }
            
            if (id == null) {
                logger.warn("Received null id for transaction update");
                return ResponseEntity.badRequest().build();
            }
            
            transaction.setId(id);
            logger.info("Updating transaction with id: {} for user: {}", id, userEmail);
            Transaction updatedTransaction = transactionService.updateOne(transaction, userEmail);
            logger.info("Successfully updated transaction with id: {}", id);
            return ResponseEntity.ok(updatedTransaction);
        } catch (Exception e) {
            logger.error("Error updating transaction with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(ApiConstants.TRANSACTIONS_BULK_CREATE)
    public ResponseEntity<List<Transaction>> createBulk(@RequestBody List<Transaction> transactions) {
        String userEmail = getCurrentUserEmail();
        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            if (transactions == null || transactions.isEmpty()) {
                logger.warn("Received empty or null transactions list for bulk creation");
                return ResponseEntity.badRequest().build();
            }
            
            logger.info("Creating {} transactions in bulk for user: {}", transactions.size(), userEmail);
            List<Transaction> createdTransactions = transactionService.createBulk(transactions, userEmail);
            logger.info("Successfully created {} transactions", createdTransactions.size());
            return ResponseEntity.ok(createdTransactions);
        } catch (Exception e) {
            logger.error("Error creating transactions in bulk: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(ApiConstants.TRANSACTIONS_SEARCH)
    public ResponseEntity<CriteriaResponse> getByCriteria(@RequestBody TransactionSearchParams searchParams) {
        String userEmail = getCurrentUserEmail();
        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            if (searchParams == null) {
                logger.warn("Received null search parameters for transaction search");
                return ResponseEntity.badRequest().build();
            }
            
            logger.info("Searching transactions with criteria for user: {}", userEmail);
            CriteriaResponse response = transactionService.getByCriteria(searchParams, userEmail);
            logger.info("Successfully found transactions matching criteria");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error searching transactions by criteria: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(ApiConstants.TRANSACTIONS_CURRENT_BALANCE)
    public ResponseEntity<CurrentBalanceResponse> getCurrentBalance() {
        String userEmail = getCurrentUserEmail();
        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            logger.info("Getting current account balance for user: {}", userEmail);
            CurrentBalanceResponse response = transactionService.getCurrentBalance(userEmail);
            logger.info("Successfully retrieved current balance: {}", response.getCurrentBalance());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting current balance: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

}