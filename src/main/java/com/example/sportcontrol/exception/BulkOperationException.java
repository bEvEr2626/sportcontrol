package com.example.sportcontrol.exception;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class BulkOperationException extends RuntimeException {

    private final int successCount;
    private final Map<String, String> failedItems;

    public BulkOperationException(String message, int successCount, Map<String, String> failedItems) {
        super(message);
        this.successCount = successCount;
        this.failedItems = Collections.unmodifiableMap(new LinkedHashMap<>(failedItems));
    }

    public int getSuccessCount() {
        return successCount;
    }

    public Map<String, String> getFailedItems() {
        return failedItems;
    }
}