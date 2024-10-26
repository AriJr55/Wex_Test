--liquibase formatted sql

--changeset Ari.Junior:1 labels:create-transactions-table context:create-transactions-table
--comment: create-transactions-tables
create table transactions (
    id integer primary key autoincrement not null,
    description varchar(50) not null,
    transaction_date DATETIME not null,
    purchase_amount DOUBLE(2)
);
--rollback DROP TABLE transactions;


