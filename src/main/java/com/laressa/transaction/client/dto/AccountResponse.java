package com.laressa.transaction.client.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountResponse (

        UUID accountId,
        String clientName,
        String document,
        BigDecimal balance,
        String accountType,
        String accountStatus
) {
}
