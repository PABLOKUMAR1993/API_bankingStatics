package com.banking.statistics.constant;

public class ApiConstants {
    
    public static final String API_BASE_PATH = "/api/v1";
    
    public static final String CATEGORIES_BASE_PATH = API_BASE_PATH + "/categories";
    public static final String CATEGORIES_BULK_UPDATE = "/bulk-update";
    public static final String CATEGORIES_BULK_CREATE = "/bulk-create";
    
    public static final String TRANSACTIONS_BASE_PATH = API_BASE_PATH + "/transactions";
    public static final String TRANSACTIONS_UPDATE = "/{id}";
    public static final String TRANSACTIONS_BULK_CREATE = "/bulk-create";
    public static final String TRANSACTIONS_SEARCH = "/search";
    public static final String TRANSACTIONS_CURRENT_BALANCE = "/current-balance";
    public static final String TRANSACTIONS_CHART_DATA = "/chart-data";
    
    private ApiConstants() {
    }

}