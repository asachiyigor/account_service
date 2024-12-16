CREATE TABLE account
(
    account_id  VARCHAR(20) PRIMARY KEY,
    owner_id    BIGINT      NOT NULL,
    owner_type  VARCHAR(32) NOT NULL,
    type        VARCHAR(64) NOT NULL,
    currency    VARCHAR(32) NOT NULL,
    status      VARCHAR(32) NOT NULL,
    created_at  timestamptz DEFAULT current_timestamp,
    updated_at  timestamptz DEFAULT current_timestamp,
    closed_at   timestamptz DEFAULT current_timestamp,
    version     INTEGER     NOT NULL,
    is_verified BOOLEAN     NOT NULL,
    notes       VARCHAR(4096),

    CONSTRAINT account_number_length CHECK (CHAR_LENGTH(account_id) BETWEEN 12 AND 20),
    CONSTRAINT account_number_digits CHECK (account_id ~ '^[0-9]+$')
);

CREATE INDEX idx_account_owner_id ON account (owner_id);