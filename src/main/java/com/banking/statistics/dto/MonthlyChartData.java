package com.banking.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyChartData {
    
    private String period;
    private BigDecimal totalIngresos;
    private BigDecimal totalPagos;
    private BigDecimal netBalance;
    
}