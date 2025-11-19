package com.banking.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartDataResponse {
    
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private List<MonthlyChartData> monthlyData;
    
}