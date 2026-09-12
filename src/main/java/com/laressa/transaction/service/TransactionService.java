package com.laressa.transaction.service;

import com.laressa.transaction.domain.Transaction;
import com.laressa.transaction.dto.TransactionRequestDTO;
import com.laressa.transaction.exception.TransactionNotFoundException;
import com.laressa.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction createTransaction(TransactionRequestDTO request) {
        Transaction transaction = new Transaction(
                request.transactionType(),
                request.originAccountId(),
                request.destinationAccountId(),
                request.amount()
        );
        return transactionRepository.save(transaction);
    }

    public Transaction findById(UUID transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transação não encontrada: " + transactionId));
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }
}