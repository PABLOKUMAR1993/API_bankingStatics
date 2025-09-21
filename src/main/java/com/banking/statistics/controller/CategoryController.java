package com.banking.statistics.controller;

import com.banking.statistics.constant.ApiConstants;
import com.banking.statistics.entity.Category;
import com.banking.statistics.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.CATEGORIES_BASE_PATH)
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        String userEmail = getCurrentUserEmail();
        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            logger.info("Getting all categories for user: {}", userEmail);
            List<Category> categories = categoryService.getAll();
            logger.info("Successfully retrieved {} categories", categories.size());
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            logger.error("Error getting all categories: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(ApiConstants.CATEGORIES_BULK_UPDATE)
    public ResponseEntity<List<Category>> updateBulk(@RequestBody List<Category> categories) {
        String userEmail = getCurrentUserEmail();
        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            if (categories == null || categories.isEmpty()) {
                logger.warn("Received empty or null categories list for bulk update");
                return ResponseEntity.badRequest().build();
            }
            
            logger.info("Updating {} categories in bulk for user: {}", categories.size(), userEmail);
            List<Category> updatedCategories = categoryService.updateBulk(categories);
            logger.info("Successfully updated {} categories", updatedCategories.size());
            return ResponseEntity.ok(updatedCategories);
        } catch (Exception e) {
            logger.error("Error updating categories in bulk: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(ApiConstants.CATEGORIES_BULK_CREATE)
    public ResponseEntity<List<Category>> createBulk(@RequestBody List<Category> categories) {
        String userEmail = getCurrentUserEmail();
        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            if (categories == null || categories.isEmpty()) {
                logger.warn("Received empty or null categories list for bulk creation");
                return ResponseEntity.badRequest().build();
            }
            
            logger.info("Creating {} categories in bulk for user: {}", categories.size(), userEmail);
            List<Category> createdCategories = categoryService.createBulk(categories);
            logger.info("Successfully created {} categories", createdCategories.size());
            return ResponseEntity.ok(createdCategories);
        } catch (Exception e) {
            logger.error("Error creating categories in bulk: {}", e.getMessage(), e);
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