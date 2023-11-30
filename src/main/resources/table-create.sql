DROP TABLE currencies;
DROP TABLE exchange_rates;

CREATE TABLE IF NOT EXISTS currencies
(
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    code      VARCHAR NOT NULL,
    full_name VARCHAR NOT NULL,
    sign      VARCHAR NOT NULL,

    UNIQUE (code)
);

CREATE TABLE IF NOT EXISTS exchange_rates
(
    id                 INTEGER PRIMARY KEY AUTOINCREMENT,
    base_currency_id   INTEGER        NOT NULL,
    target_currency_id INTEGER        NOT NULL,
    rate               DECIMAL(10, 6) NOT NULL,

    FOREIGN KEY (base_currency_id) REFERENCES currencies (id) ON DELETE CASCADE,
    FOREIGN KEY (target_currency_id) REFERENCES currencies (id) ON DELETE CASCADE,

    UNIQUE (base_currency_id, target_currency_id)
);

INSERT INTO currencies (code, full_name, sign)
VALUES ('CNY', 'Yuan Renminbi', '¥'),
       ('EUR', 'Euro', '€'),
       ('RUB', 'Russian Ruble', '₽'),
       ('USD', 'US Dollar', '$');

INSERT INTO exchange_rates(base_currency_id, target_currency_id, rate)
VALUES (2, 4, 1.06),
       (4, 2, 0.945572),
       (1, 3, 13.07),
       (3, 1, 0.076518),
       (3, 4, 0.010427),
       (4, 3, 95.91),
       (1, 4, 0.136268),
       (4, 1, 7.34);
