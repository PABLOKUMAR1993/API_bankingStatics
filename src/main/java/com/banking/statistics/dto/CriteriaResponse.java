package com.banking.statistics.dto;

import com.banking.statistics.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CriteriaResponse {
    
    private Long totalElementsFound;
    private Integer elementsPerPage;
    private Integer currentPageNumber;
    private List<Transaction> elements;

}