CREATE TABLE IF NOT EXISTS owner
(
    id         BIGSERIAL PRIMARY KEY,
    type       VARCHAR(32) NOT NULL,
    owner_id   BIGINT      NOT NULL UNIQUE,
    created_at timestamptz DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz
);

CREATE TABLE IF NOT EXISTS account
(
    id             BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL UNIQUE CHECK ( LENGTH(account_number) >= 12
                                                           AND account_number SIMILAR TO '[0-9]+'),
    owner_id       BIGINT      NOT NULL,
    type           VARCHAR(64) NOT NULL,
    currency       VARCHAR(32) NOT NULL,
    status         VARCHAR(32) NOT NULL,
    created_at     TIMESTAMPTZ          DEFAULT current_timestamp,
    updated_at     TIMESTAMPTZ,
    closed_at      TIMESTAMPTZ,
    version        BIGINT      NOT NULL DEFAULT 0,
    is_verified    BOOLEAN     NOT NULL,
    notes          VARCHAR(4096),

    CONSTRAINT fk_account_owner FOREIGN KEY (owner_id) REFERENCES owner (id)
);

CREATE INDEX idx_owner_id ON account (owner_id);
CREATE INDEX idx_account_number ON account (account_number);