--liquibase formatted sql
--changeset nvoxland:1
alter table link_table
add column is_github_link boolean