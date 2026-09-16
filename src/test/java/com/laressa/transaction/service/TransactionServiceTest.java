package com.laressa.transaction.service;

import com.laressa.transaction.client.AccountClient;
import com.laressa.transaction.domain.Transaction;
import com.laressa.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountClient accountClient;

    @InjectMocks
    private TransactionService transactionService;

    private UUID originAccountId;
    private UUID destinationAccountId;

    @BeforeEach
    void setUp() {
        originAccountId = UUID.randomUUID();
        destinationAccountId = UUID.randomUUID();

        when(transactionRepository.save(any(Transaction.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
    }
}