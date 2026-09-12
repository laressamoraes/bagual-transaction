package com.laressa.transaction.dto;

import com.laressa.transaction.domain.Transaction;
import com.laressa.transaction.domain.TransactionStatus;
import com.laressa.transaction.domain.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionResponseDTO(

    UUID transactionId,
    TransactionType transactionType,
    UUID originAccountId,
    UUID destinationAccountId,
    BigDecimal amount,
    TransactionStatus transactionStatus
) {

    public static TransactionResponseDTO fromEntity(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getTransactionId(),
                transaction.getTransactionType(),
                transaction.getOriginAccountId(),
                transaction.getDestinationAccountId(),
                transaction.getAmount(),
                transaction.getTransactionStatus()
        );
    }
}