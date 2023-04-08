--liquibase formatted sql
--changeset nvoxland:1
create table user_links_table
(
    user_chat_id int,
    link_id int,
    foreign key (user_chat_id) references user_table (chat_id),
    foreign key (link_id) references link_table (id)
);