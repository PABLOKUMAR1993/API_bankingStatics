package com.banking.statics.service.impl;

import com.banking.statics.entity.Category;
import com.banking.statics.repository.CategoryRepository;
import com.banking.statics.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    @Override
    public List<Category> getAll() {
        try {
            log.info("Retrieving all categories");
            List<Category> categories = categoryRepository.findAll();
            log.info("Successfully retrieved {} categories", categories.size());
            return categories;
        } catch (Exception e) {
            log.error("Error retrieving all categories: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve categories", e);
        }
    }
    
    @Override
    @Transactional
    public List<Category> updateBulk(List<Category> categories) {
        try {
            log.info("Updating {} categories in bulk", categories.size());
            List<Category> updatedCategories = categoryRepository.saveAll(categories);
            log.info("Successfully updated {} categories", updatedCategories.size());
            return updatedCategories;
        } catch (Exception e) {
            log.error("Error updating categories in bulk: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update categories", e);
        }
    }
    
    @Override
    @Transactional
    public List<Category> createBulk(List<Category> categories) {
        try {
            log.info("Creating {} categories in bulk", categories.size());
            List<Category> createdCategories = categoryRepository.saveAll(categories);
            log.info("Successfully created {} categories", createdCategories.size());
            return createdCategories;
        } catch (Exception e) {
            log.error("Error creating categories in bulk: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create categories", e);
        }
    }

}