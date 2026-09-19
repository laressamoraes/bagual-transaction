package com.laressa.transaction.event;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionEvent(

        UUID transactionId,
        String transactionType,
        UUID originAccountId,
        UUID destinationAccountId,
        BigDecimal amount,
        String transactionStatus
) {
}