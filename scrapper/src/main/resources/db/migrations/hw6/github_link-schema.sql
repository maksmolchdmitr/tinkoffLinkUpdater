--liquibase formatted sql
--changeset nvoxland:1
create table github_link_table
(
    link_url varchar(256) primary key,
    branch_count int not null,
    foreign key (link_url) references link_table(url) on delete cascade
)