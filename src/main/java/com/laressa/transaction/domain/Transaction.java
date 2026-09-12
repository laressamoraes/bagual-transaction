package com.laressa.transaction.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transactionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private UUID originAccountId;

    private UUID destinationAccountId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus transactionStatus;

    public Transaction() {
    }

    public Transaction(TransactionType transactionType, UUID originAccountId, UUID destinationAccountId, BigDecimal amount) {
        validatePositiveAmount(amount);
        validateDestination(transactionType, destinationAccountId);

        this.transactionType = transactionType;
        this.originAccountId = originAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.transactionStatus = TransactionStatus.PENDENTE;
    }

    public void complete() {
        validatePendingStatus();
        this.transactionStatus = TransactionStatus.CONCLUIDA;
    }

    public void fail() {
        validatePendingStatus();
        this.transactionStatus = TransactionStatus.FALHOU;
    }

    private void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor deve ser positivo! ");
        }
    }

    private void validateDestination(TransactionType transactionType, UUID destinationAccountId) {
        if (transactionType == TransactionType.TRANSFERENCIA && destinationAccountId == null) {
            throw new IllegalArgumentException("Transferência exige uma conta de destino!");
        }
        if (transactionType != TransactionType.TRANSFERENCIA && destinationAccountId != null) {
            throw new IllegalArgumentException("Apenas transferências podem ter conta de destino!");
        }
    }

    private void validatePendingStatus() {
        if (this.transactionStatus !=  TransactionStatus.PENDENTE) {
            throw new IllegalStateException("Apenas transações pendentes podem ter seu status alterado!");
        }
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public UUID getOriginAccountId() {
        return originAccountId;
    }

    public UUID getDestinationAccountId() {
        return destinationAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }
}