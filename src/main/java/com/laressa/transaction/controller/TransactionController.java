package com.laressa.transaction.controller;

import com.laressa.transaction.domain.Transaction;
import com.laressa.transaction.dto.TransactionRequestDTO;
import com.laressa.transaction.dto.TransactionResponseDTO;
import com.laressa.transaction.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@Valid @RequestBody TransactionRequestDTO transactionRequest) {
        Transaction transaction = transactionService.createTransaction(transactionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionResponseDTO.fromEntity(transaction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> findById(@PathVariable UUID id) {
        Transaction transaction = transactionService.findById(id);
        return ResponseEntity.ok(TransactionResponseDTO.fromEntity(transaction));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> findAll() {
        List<TransactionResponseDTO> transactions = transactionService.findAll()
                .stream()
                .map(TransactionResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(transactions);
    }
}