--liquibase formatted sql
--changeset nvoxland:1
create table link_table
(
    url varchar(256) not null primary key ,
    last_update timestamp
);