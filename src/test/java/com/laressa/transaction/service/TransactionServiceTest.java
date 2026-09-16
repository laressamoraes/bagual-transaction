package com.laressa.transaction.service;

import com.laressa.transaction.client.AccountClient;
import com.laressa.transaction.domain.Transaction;
import com.laressa.transaction.domain.TransactionStatus;
import com.laressa.transaction.domain.TransactionType;
import com.laressa.transaction.dto.TransactionRequestDTO;
import com.laressa.transaction.exception.AccountIntegrationException;
import com.laressa.transaction.exception.TransactionNotFoundException;
import com.laressa.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    }

    @Test
    void shouldDepositSuccessfully() {
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TransactionRequestDTO request = new TransactionRequestDTO(
                TransactionType.DEPOSITO,
                originAccountId,
                null,
                new BigDecimal("100.00")
        );

        Transaction result = transactionService.createTransaction(request);

        assertThat(result.getTransactionStatus()).isEqualTo(TransactionStatus.CONCLUIDA);
        assertThat(result.getTransactionType()).isEqualTo(TransactionType.DEPOSITO);
        assertThat(result.getOriginAccountId()).isEqualTo(originAccountId);
        assertThat(result.getAmount()).isEqualByComparingTo("100.00");

        verify(accountClient).credit(originAccountId, new BigDecimal("100.00"));
        verify(accountClient, never()).debit(any(), any());
    }

    @Test
    void shouldWithdrawSuccessfully() {
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TransactionRequestDTO request = new TransactionRequestDTO(
                TransactionType.SAQUE,
                originAccountId,
                null,
                new BigDecimal("50.00")
        );

        Transaction result = transactionService.createTransaction(request);

        assertThat(result.getTransactionStatus()).isEqualTo(TransactionStatus.CONCLUIDA);
        assertThat(result.getTransactionType()).isEqualTo(TransactionType.SAQUE);
        assertThat(result.getOriginAccountId()).isEqualTo(originAccountId);
        assertThat(result.getAmount()).isEqualByComparingTo("50.00");

        verify(accountClient).debit(originAccountId, new BigDecimal("50.00"));
        verify(accountClient, never()).credit(any(), any());
    }

    @Test
    void shouldTransferSuccessfully() {
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TransactionRequestDTO request = new TransactionRequestDTO(
                TransactionType.TRANSFERENCIA,
                originAccountId,
                destinationAccountId,
                new BigDecimal("30.00")
        );

        Transaction result = transactionService.createTransaction(request);

        assertThat(result.getTransactionStatus()).isEqualTo(TransactionStatus.CONCLUIDA);
        assertThat(result.getTransactionType()).isEqualTo(TransactionType.TRANSFERENCIA);
        assertThat(result.getOriginAccountId()).isEqualTo(originAccountId);
        assertThat(result.getDestinationAccountId()).isEqualTo(destinationAccountId);
        assertThat(result.getAmount()).isEqualByComparingTo("30.00");

        verify(accountClient).debit(originAccountId, new BigDecimal("30.00"));
        verify(accountClient).credit(destinationAccountId, new BigDecimal("30.00"));
    }

    @Test
    void shouldFailWhenTransferFails() {
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TransactionRequestDTO request = new TransactionRequestDTO(
                TransactionType.TRANSFERENCIA,
                originAccountId,
                destinationAccountId,
                new BigDecimal("30.00")
        );

        doThrow(new AccountIntegrationException(HttpStatus.UNPROCESSABLE_ENTITY, "Saldo insuficiente!"))
                .when(accountClient).debit(originAccountId, new BigDecimal("30.00"));

        assertThatThrownBy(() -> transactionService.createTransaction(request))
                .isInstanceOf(AccountIntegrationException.class);

        verify(accountClient).debit(originAccountId, new BigDecimal("30.00"));
        verify(accountClient, never()).credit(any(), any());
    }

    @Test
    void shouldCompensateWhenTransferFails() {
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TransactionRequestDTO request = new TransactionRequestDTO(
                TransactionType.TRANSFERENCIA,
                originAccountId,
                destinationAccountId,
                new BigDecimal("30.00")
        );

        doThrow(new AccountIntegrationException(HttpStatus.NOT_FOUND, "Conta de destino não encontrada!"))
                .when(accountClient).credit(destinationAccountId, new BigDecimal("30.00"));

        assertThatThrownBy(() -> transactionService.createTransaction(request))
                .isInstanceOf(AccountIntegrationException.class);

        verify(accountClient).debit(originAccountId, new BigDecimal("30.00"));
        verify(accountClient).credit(destinationAccountId, new BigDecimal("30.00"));
        verify(accountClient).credit(originAccountId, new BigDecimal("30.00"));
    }

    @Test
    void shouldFindTransactionByIdSuccessfully() {
        UUID transactionId = UUID.randomUUID();
        Transaction transaction = new Transaction(
                TransactionType.DEPOSITO,
                originAccountId,
                null,
                new BigDecimal("100.00")
        );

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        Transaction result = transactionService.findById(transactionId);

        assertThat(result).isEqualTo(transaction);
    }

    @Test
    void shouldThrowExceptionWhenTransactionNotFound() {
        UUID transactionId = UUID.randomUUID();
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.findById(transactionId))
                .isInstanceOf(TransactionNotFoundException.class);
    }

    @Test
    void shouldListAllTransactions() {
        Transaction transaction = new Transaction(
                TransactionType.DEPOSITO,
                originAccountId,
                null,
                new BigDecimal("100.00")
        );
        when(transactionRepository.findAll()).thenReturn(List.of(transaction));

        List<Transaction> result = transactionService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(transaction);
    }
}