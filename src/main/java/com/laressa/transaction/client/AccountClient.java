package com.laressa.transaction.client;

import com.laressa.transaction.client.dto.AccountAmountRequest;
import com.laressa.transaction.client.dto.AccountResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class AccountClient {

    private final RestClient accountRestClient;

    public AccountClient(RestClient accountRestClient) {
        this.accountRestClient = accountRestClient;
    }

    public AccountResponse debit(UUID accountId, BigDecimal amount) {
        return accountRestClient.patch()
                .uri("accounts/{id}/debit", accountId)
                .body(new AccountAmountRequest(amount))
                .retrieve()
                .body(AccountResponse.class);
    }

    public AccountResponse credit(UUID accountId, BigDecimal amount) {
        return accountRestClient.patch()
                .uri("accounts/{id}/credit", accountId)
                .body(new AccountAmountRequest(amount))
                .retrieve()
                .body(AccountResponse.class);
    }
}