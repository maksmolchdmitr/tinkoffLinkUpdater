package org.example;

import org.example.dao.LinkDao;
import org.example.dao.UserDao;
import org.example.dao.UserLinksDao;
import org.example.model.Link;
import org.example.model.User;
import org.example.model.UserLinks;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DatabaseScrapperTest extends IntegrationEnvironment {

    @Autowired
    private UserDao userDao;
    @Autowired
    private LinkDao linkDao;
    @Autowired
    private UserLinksDao userLinksDao;

    @Test
    @Transactional
    @Rollback
    void addLinkForUser(){
        //add User in table
        userDao.add(new User(0, "Maks"));
        //add Link in table
        linkDao.add(new Link(0, "https://github.com/maksmolchdmitr/EmemeBot",
                new Timestamp(0)));
        //add link for user
        userLinksDao.add(new UserLinks(0, 0));

        userDao.findAll()
                .forEach(user -> assertEquals(user.username(), "Maks"));
        linkDao.findAll()
                .forEach(link -> assertEquals(link.url(),
                        "https://github.com/maksmolchdmitr/EmemeBot"));
        userLinksDao.findAll().
                forEach(userLinks ->{
                    assertEquals(userLinks.linkId(), 0);
                    assertEquals(userLinks.userChatId(), 0);
                });
    }

}
