package org.example.service;

import org.example.model.UserLinks;

import java.util.List;

public interface UserLinksService {
    UserLinks add(long chatId, String url);
    UserLinks remove(long chatId, String url);
    List<UserLinks> findByChatId(long chatId);
}
