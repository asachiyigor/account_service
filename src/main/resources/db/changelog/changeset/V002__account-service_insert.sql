INSERT INTO account (account_number, owner_id, owner_type, type, currency, status, created_at, updated_at, closed_at, version, balance, is_verified, notes)
VALUES
    ('123456789012', 1, 'USER', 'CURRENT_ACCOUNT', 'USD', 'ACTIVE', current_timestamp, current_timestamp, NULL, 1, 1000.50, TRUE, 'Personal account'),
    ('234567890123', 2, 'USER', 'SAVINGS_ACCOUNT', 'RUB', 'ACTIVE', current_timestamp, current_timestamp, NULL, 1, 5000.75, TRUE, 'Savings account'),
    ('345678901234', 3, 'USER', 'CURRENT_ACCOUNT', 'EUR', 'ACTIVE', current_timestamp, current_timestamp, NULL, 1, 1500.00, FALSE, 'Business account'),
    ('456789012345', 4, 'PROJECT', 'FOREIGN_CURRENCY_ACCOUNT', 'USD', 'FROZEN', current_timestamp, current_timestamp, NULL, 1, 0.00, FALSE, 'Project account'),
    ('567890123456', 1, 'USER', 'SAVINGS_ACCOUNT', 'USD', 'ACTIVE', current_timestamp, current_timestamp, NULL, 1, 3500.20, TRUE, 'Vacation savings');
