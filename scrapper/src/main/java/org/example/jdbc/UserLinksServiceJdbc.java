package org.example.jdbc;

import org.example.dao.LinkDao;
import org.example.dao.UserLinksDao;
import org.example.model.Link;
import org.example.model.UserLinks;
import org.example.service.UserLinksService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserLinksServiceJdbc implements UserLinksService {
    private final UserLinksDao userLinksDao;
    private final LinkDao linkDao;

    public UserLinksServiceJdbc(UserLinksDao userLinksDao, LinkDao linkDao) {
        this.userLinksDao = userLinksDao;
        this.linkDao = linkDao;
    }

    @Override
    @Transactional
    public UserLinks add(long chatId, String url) {
        Link newLink = linkDao.add(new Link(url));
        return userLinksDao.add(new UserLinks(chatId, newLink.url())).orElse(null);
    }

    @Override
    @Transactional
    public UserLinks remove(long chatId, String url) {
        try{
            return userLinksDao.removeWithLink(new UserLinks(chatId, url));
        }catch (EmptyResultDataAccessException ignored){
            return null;
        }
    }

    @Override
    public List<UserLinks> findByChatId(long chatId) {
        return userLinksDao.findByChatId(chatId);
    }
}
