package com.laressa.transaction.service;

import com.laressa.transaction.client.AccountClient;
import com.laressa.transaction.domain.Transaction;
import com.laressa.transaction.dto.TransactionRequestDTO;
import com.laressa.transaction.exception.TransactionNotFoundException;
import com.laressa.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountClient accountClient;

    public TransactionService(TransactionRepository transactionRepository, AccountClient accountClient) {
        this.transactionRepository = transactionRepository;
        this.accountClient = accountClient;
    }

    public Transaction createTransaction(TransactionRequestDTO request) {
        Transaction transaction = new Transaction(
                request.transactionType(),
                request.originAccountId(),
                request.destinationAccountId(),
                request.amount()
        );
        transaction = transactionRepository.save(transaction);

        try {
            processTransaction(transaction);
            transaction.complete();
        } catch (Exception ex) {
            transaction.fail();
            transactionRepository.save(transaction);
            throw ex;
        }

        return transactionRepository.save(transaction);
    }

    private void processTransaction(Transaction transaction) {
        switch (transaction.getTransactionType()) {
            case DEPOSITO -> accountClient.credit(transaction.getOriginAccountId(), transaction.getAmount());
            case SAQUE -> accountClient.debit(transaction.getOriginAccountId(), transaction.getAmount());
            case TRANSFERENCIA -> processTransfer(transaction);
        }
    }

    private void processTransfer(Transaction transaction) {
        UUID originAccountId = transaction.getOriginAccountId();
        UUID destinationAccountId = transaction.getDestinationAccountId();
        BigDecimal amount = transaction.getAmount();

        accountClient.debit(originAccountId, amount);

        try {
            accountClient.credit(destinationAccountId, amount);
        } catch (Exception ex) {
            accountClient.credit(originAccountId, amount);

            throw ex;
        }
    }

    public Transaction findById(UUID transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transação não encontrada: " + transactionId));
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }
}