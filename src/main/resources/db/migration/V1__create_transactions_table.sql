CREATE TABLE transactions (
    transaction_id          UUID PRIMARY KEY,
    transaction_type        VARCHAR(50) NOT NULL,
    origin_account_id       UUID NOT NULL,
    destination_account_id  UUID,
    amount                  NUMERIC(19, 2) NOT NULL,
    transaction_status      VARCHAR(50) NOT NULL
);