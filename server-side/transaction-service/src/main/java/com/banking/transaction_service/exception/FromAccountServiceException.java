package com.banking.transaction_service.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FromAccountServiceException extends RuntimeException {
    private final int statusCode;
    private final String errorNumber;

    public FromAccountServiceException(String message, int statusCode, String errorBody) {
        super(message);
        this.statusCode = statusCode;
        this.errorNumber = parseErrorNumber(errorBody);
    }

    private String parseErrorNumber(String errorBody) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(errorBody);
            return root.path("errorNumber").asText(null);
        } catch (Exception e) {
            return null;
        }
    }

    public int getStatusCode() { return statusCode; }
    public String getErrorNumber() { return errorNumber; }
}
