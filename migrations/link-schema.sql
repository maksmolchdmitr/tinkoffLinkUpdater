--liquibase formatted sql
--changeset nvoxland:1
create table link_table
(
    id serial not null primary key,
    url varchar(256) not null unique
);