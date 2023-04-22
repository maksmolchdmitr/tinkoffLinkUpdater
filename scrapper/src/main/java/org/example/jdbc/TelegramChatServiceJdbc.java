package org.example.jdbc;

import org.example.dao.LinkDao;
import org.example.dao.UserDao;
import org.example.dao.UserLinksDao;
import org.example.model.User;
import org.example.model.UserLinks;
import org.example.service.TelegramChatService;

public class TelegramChatServiceJdbc implements TelegramChatService {
    private final UserDao userDao;
    private final UserLinksDao userLinksDao;
    private final LinkDao linkDao;

    public TelegramChatServiceJdbc(UserDao userDao, UserLinksDao userLinksDao, LinkDao linkDao) {
        this.userDao = userDao;
        this.userLinksDao = userLinksDao;
        this.linkDao = linkDao;
    }

    @Override
    public void register(long chatId) {
        userDao.add(new User(chatId));
    }

    @Override
    public void unregister(long chatId) {
        for(UserLinks userLinks:userLinksDao.findByChatId(chatId)){
            linkDao.removeIfWithOneUser(userLinks.linkUrl());
        }
        userDao.remove(chatId);
    }
}
