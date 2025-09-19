package com.banking.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSearchParams {
    
    private Integer pageNumber = 0;
    private Integer pageSize = 10;
    private LocalDate operationDateFrom;
    private LocalDate operationDateTo;
    private LocalDate valueDateFrom;
    private LocalDate valueDateTo;
    private String concept;
    private BigDecimal paymentAmountFrom;
    private BigDecimal paymentAmountTo;
    private BigDecimal incomeAmountFrom;
    private BigDecimal incomeAmountTo;
    private List<Long> categoryIds;

}