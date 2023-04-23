package org.example.dao;

import org.example.IntegrationEnvironment;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserLinksDaoTest extends IntegrationEnvironment {

    @Autowired
    private UserDao userDao;
    @Autowired
    private LinkDao linkDao;
    @Autowired
    private UserLinksDao userLinksDao;
    @Test
    @Transactional
    @Rollback
    void add() {
        userDao.add(new User(0, "Maks"));
        linkDao.add(new Link("https://stackoverflow.com/questions/43569781/unable-to-start-docker-service-with-error-failed-to-start-docker-service-unit",
                new Timestamp(0)));
        userLinksDao.add(new UserLinks(0, "https://stackoverflow.com/questions/43569781/unable-to-start-docker-service-with-error-failed-to-start-docker-service-unit"));
        userLinksDao.findAll()
                .forEach(userLinks -> {
                    assertEquals(userLinks.linkUrl(), "https://stackoverflow.com/questions/43569781/unable-to-start-docker-service-with-error-failed-to-start-docker-service-unit");
                    assertEquals(userLinks.userChatId(), 0);
                });
    }

    @Test
    @Transactional
    @Rollback
    void removeByUserChatId() {
        userDao.add(new User(0, "Maks"));
        linkDao.add(new Link("https://stackoverflow.com/questions/43569781/unable-to-start-docker-service-with-error-failed-to-start-docker-service-unit",
                new Timestamp(0)));
        userLinksDao.add(new UserLinks(0, "https://stackoverflow.com/questions/43569781/unable-to-start-docker-service-with-error-failed-to-start-docker-service-unit"));
        userLinksDao.findAll()
                .forEach(userLinks -> {
                    assertEquals(userLinks.linkUrl(), "https://stackoverflow.com/questions/43569781/unable-to-start-docker-service-with-error-failed-to-start-docker-service-unit");
                    assertEquals(userLinks.userChatId(), 0);
                });
        userLinksDao.removeByUserChatId(0);
        userLinksDao.findAll()
                .forEach(
                        userLinks ->{
                            throw new RuntimeException("Link table is not empty but it actually must be empty!");
                        }
                );
    }

    @Test
    @Transactional
    @Rollback
    void removeByLinkId() {
        userDao.add(new User(0, "Maks"));
        linkDao.add(new Link("https://stackoverflow.com/questions/43569781/unable-to-start-docker-service-with-error-failed-to-start-docker-service-unit",
                new Timestamp(0)));
        userLinksDao.add(new UserLinks(0, "https://stackoverflow.com/questions/43569781/unable-to-start-docker-service-with-error-failed-to-start-docker-service-unit"));
        userLinksDao.findAll()
                .forEach(userLinks -> {
                    assertEquals(userLinks.linkUrl(), "https://stackoverflow.com/questions/43569781/unable-to-start-docker-service-with-error-failed-to-start-docker-service-unit");
                    assertEquals(userLinks.userChatId(), 0);
                });
        userLinksDao.removeByLinkUrl("https://stackoverflow.com/questions/43569781/unable-to-start-docker-service-with-error-failed-to-start-docker-service-unit");
        userLinksDao.findAll()
                .forEach(
                        userLinks -> {
                            throw new RuntimeException("Link table is not empty but it actually must be empty!");
                        }
                );
    }

    @Test
    @Transactional
    @Rollback
    void findAll() {
        userDao.add(new User(0, "Maks"));
        userDao.add(new User(1, "Nikita"));
        userDao.add(new User(2, "Dima"));
        linkDao.add(new Link("https://stackoverflow.com/questions/43569781/unable-to-start-docker-service-with-error-failed-to-start-docker-service-unit",
                new Timestamp(0)));
        linkDao.add(new Link("https://github.com/maksmolchdmitr/tinkoffLinkUpdater",
                new Timestamp(0)));
        linkDao.add(new Link("https://github.com/maksmolchdmitr/EmemeBot",
                new Timestamp(0)));
        userLinksDao.add(new UserLinks(0, "https://stackoverflow.com/questions/43569781/unable-to-start-docker-service-with-error-failed-to-start-docker-service-unit"));
        userLinksDao.add(new UserLinks(0, "https://github.com/maksmolchdmitr/tinkoffLinkUpdater"));
        userLinksDao.add(new UserLinks(0, "https://github.com/maksmolchdmitr/EmemeBot"));
        userLinksDao.add(new UserLinks(1, "https://github.com/maksmolchdmitr/EmemeBot"));
        userLinksDao.add(new UserLinks(2, "https://stackoverflow.com/questions/43569781/unable-to-start-docker-service-with-error-failed-to-start-docker-service-unit"));
        userLinksDao.removeByUserChatId(0);
        userLinksDao.removeByUserChatId(1);
        userLinksDao.removeByUserChatId(2);
        userLinksDao.findAll()
                .forEach(
                        userLinks -> {
                            throw new RuntimeException("UserLinks table is not empty but it actually must be empty!");
                        }
                );
    }

    @Test
    @Transactional
    @Rollback
    void removeUserLink(){
        userDao.add(new User(0));
        linkDao.add(new Link("link"));
        userLinksDao.add(new UserLinks(0, "link"));
        userLinksDao.remove(new UserLinks(0, "link"));
    }

    @Test
    @Transactional
    @Rollback
    void removeWithLinkTest(){
        userDao.add(new User(0));
        linkDao.add(new Link("link1"));
        userLinksDao.add(new UserLinks(0, "link1"));
        userLinksDao.removeWithLink(new UserLinks(0, "link1"));
        assertTrue(linkDao.findAll().isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void addLinkWithoutRegisteredUser(){
        linkDao.add(new Link("link1"));
        userLinksDao.add(new UserLinks(0, "link1"));
    }
}