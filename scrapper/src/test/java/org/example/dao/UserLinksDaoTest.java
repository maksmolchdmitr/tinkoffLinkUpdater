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
import java.util.Set;

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
        linkDao.add(new Link(0, "https://stackoverflow.com/questions/43569781/unable-to-start-docker-service-with-error-failed-to-start-docker-service-unit",
                new Timestamp(0)));
        userLinksDao.add(new UserLinks(0, 0));
        userLinksDao.findAll()
                .forEach(userLinks -> {
                    assertEquals(userLinks.linkId(), 0);
                    assertEquals(userLinks.userChatId(), 0);
                });
    }

    @Test
    @Transactional
    @Rollback
    void removeByUserChatId() {
        userDao.add(new User(0, "Maks"));
        linkDao.add(new Link(0, "https://stackoverflow.com/questions/43569781/unable-to-start-docker-service-with-error-failed-to-start-docker-service-unit",
                new Timestamp(0)));
        userLinksDao.add(new UserLinks(0, 0));
        userLinksDao.findAll()
                .forEach(userLinks -> {
                    assertEquals(userLinks.linkId(), 0);
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
        linkDao.add(new Link(0, "https://stackoverflow.com/questions/43569781/unable-to-start-docker-service-with-error-failed-to-start-docker-service-unit",
                new Timestamp(0)));
        userLinksDao.add(new UserLinks(0, 0));
        userLinksDao.findAll()
                .forEach(userLinks -> {
                    assertEquals(userLinks.linkId(), 0);
                    assertEquals(userLinks.userChatId(), 0);
                });
        userLinksDao.removeByLinkId(0);
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
        linkDao.add(new Link(0, "https://stackoverflow.com/questions/43569781/unable-to-start-docker-service-with-error-failed-to-start-docker-service-unit",
                new Timestamp(0)));
        linkDao.add(new Link(1, "https://github.com/maksmolchdmitr/tinkoffLinkUpdater",
                new Timestamp(0)));
        linkDao.add(new Link(2, "https://github.com/maksmolchdmitr/EmemeBot",
                new Timestamp(0)));
        userLinksDao.add(new UserLinks(0, 0));
        userLinksDao.add(new UserLinks(0, 1));
        userLinksDao.add(new UserLinks(0, 2));
        userLinksDao.add(new UserLinks(1, 2));
        userLinksDao.add(new UserLinks(2, 0));
        userLinksDao.findAll()
                .forEach(userLinks -> {
                    switch ((int) userLinks.userChatId()){
                        case 0 -> assertTrue(Set.of(0L, 1L, 2L).contains(userLinks.linkId()));
                        case 1 -> assertEquals(2, userLinks.linkId());
                        case 2 -> assertEquals(0, userLinks.linkId());
                        default -> throw new RuntimeException("Unknown userChatId!");
                    }
                });
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
}