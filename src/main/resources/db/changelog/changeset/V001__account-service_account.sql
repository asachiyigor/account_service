CREATE TABLE account
(
    account_id VARCHAR(20)                             NOT NULL,
    owner_id       BIGINT                                  NOT NULL,
    owner_type     VARCHAR(32)                             NOT NULL,
    type           VARCHAR(64)                             NOT NULL,
    currency       VARCHAR(32)                             NOT NULL,
    status         VARCHAR(32)                             NOT NULL,
    created_at     timestamptz DEFAULT current_timestamp,
    updated_at     timestamptz DEFAULT current_timestamp,
    closed_at      timestamptz DEFAULT current_timestamp,
    version        INTEGER                                 NOT NULL,
    balance        DECIMAL                                 NOT NULL,
    is_verified    BOOLEAN                                 NOT NULL,
    notes          VARCHAR(4096),
    CONSTRAINT pk_account PRIMARY KEY (account_id),
    CONSTRAINT account_number_length CHECK (CHAR_LENGTH(account_id) BETWEEN 12 AND 20)
);

ALTER TABLE account
    ADD CONSTRAINT uc_account_account_number UNIQUE (account_id);

CREATE INDEX idx_account_owner_id ON account(owner_id);