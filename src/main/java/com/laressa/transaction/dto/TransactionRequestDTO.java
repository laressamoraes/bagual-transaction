package com.laressa.transaction.dto;

import com.laressa.transaction.domain.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionRequestDTO(

    @NotNull(message = "O tipo de transação é obrigatório!")
    TransactionType transactionType,

    @NotNull(message = "A conta de origem obrigatória!")
    UUID originAccountId,

    UUID destinationAccountId,

    @NotNull(message = "O valor é obrigatório!")
    @Positive(message = "O valor deve ser positivo!")
    BigDecimal amount
) {
}