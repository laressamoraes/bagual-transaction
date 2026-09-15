package com.laressa.transaction.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laressa.transaction.client.dto.AccountAmountRequest;
import com.laressa.transaction.client.dto.AccountErrorResponse;
import com.laressa.transaction.client.dto.AccountResponse;
import com.laressa.transaction.exception.AccountIntegrationException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.IOException;
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
                .onStatus(status -> status.isError(), this::handleError)
                .body(AccountResponse.class);
    }

    public AccountResponse credit(UUID accountId, BigDecimal amount) {
        return accountRestClient.patch()
                .uri("accounts/{id}/credit", accountId)
                .body(new AccountAmountRequest(amount))
                .retrieve()
                .onStatus(status -> status.isError(), this::handleError)
                .body(AccountResponse.class);
    }

    private void handleError(HttpRequest request, ClientHttpResponse response) throws IOException {

        AccountErrorResponse errorBody;

        try {
            errorBody = new ObjectMapper().readValue(response.getBody(), AccountErrorResponse.class);
        } catch(Exception ex) {
            throw new AccountIntegrationException(response.getStatusCode(), "Erro ao comunicar com o account!");
        }
        throw new AccountIntegrationException(response.getStatusCode(), errorBody.message());
    }
}