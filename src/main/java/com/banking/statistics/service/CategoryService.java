package com.banking.statistics.service;

import com.banking.statistics.entity.Category;

import java.util.List;

public interface CategoryService {
    
    List<Category> getAll();
    
    List<Category> updateBulk(List<Category> categories);
    
    List<Category> createBulk(List<Category> categories);

}