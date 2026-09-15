package com.laressa.transaction.client.dto;

public record AccountErrorResponse(

        String timestamp,
        int status,
        String message
) {
}