package com.laressa.transaction.exception;

import org.springframework.http.HttpStatusCode;

public class AccountIntegrationException extends RuntimeException{

    private final HttpStatusCode statusCode;

    public AccountIntegrationException(HttpStatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }
}