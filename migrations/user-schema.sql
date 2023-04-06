--liquibase formatted sql
--changeset nvoxland:1
create table user_table
(
    chat_id int not null primary key,
    username varchar(32)
);