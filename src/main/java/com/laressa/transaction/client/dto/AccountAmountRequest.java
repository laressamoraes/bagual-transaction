package com.laressa.transaction.client.dto;

import java.math.BigDecimal;

public record AccountAmountRequest(BigDecimal amount) {
}