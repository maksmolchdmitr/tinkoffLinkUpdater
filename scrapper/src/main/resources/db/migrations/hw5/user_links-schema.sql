--liquibase formatted sql
--changeset nvoxland:1
create table user_links_table
(
    user_chat_id int,
    link_url varchar(256),
    foreign key (user_chat_id) references user_table (chat_id) on delete cascade,
    foreign key (link_url) references link_table (url) on delete cascade,
    constraint unique_url_and_chat_id unique (link_url, user_chat_id)
);