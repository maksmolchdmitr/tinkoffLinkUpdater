package org.example.model;

public record User(long chatId, String username){
    public User(long chatId){
        this(chatId, null);
    }
}
