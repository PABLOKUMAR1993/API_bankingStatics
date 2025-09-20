package com.banking.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentBalanceResponse {
    
    private BigDecimal currentBalance;
    private LocalDate lastTransactionDate;
    private boolean hasTransactions;

}