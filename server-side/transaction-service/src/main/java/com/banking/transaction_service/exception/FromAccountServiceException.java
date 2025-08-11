package com.banking.transaction_service.exception;


public class FromAccountServiceException extends RuntimeException {
    private final int statusCode;
    private final String errorBody;

    public FromAccountServiceException(String message, int statusCode, String errorBody) {
        super(message);
        this.statusCode = statusCode;
        this.errorBody = errorBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorBody() {
        return errorBody;
    }
}
