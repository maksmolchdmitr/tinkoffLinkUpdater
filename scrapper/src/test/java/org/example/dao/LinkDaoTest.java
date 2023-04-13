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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LinkDaoTest extends IntegrationEnvironment {
    @Autowired
    private LinkDao linkDao;
    @Autowired
    private UserLinksDao userLinksDao;
    @Autowired
    private UserDao userDao;

    @Test
    @Transactional
    @Rollback
    void add() {
        Link newLink = linkDao.add(new Link("https://github.com/maksmolchdmitr/EmemeBot"));
        linkDao.add(new Link("https://github.com/maksmolchdmitr/EmemeBot"));
        assertNull(newLink.lastUpdate());
        assertEquals(newLink.url(), "https://github.com/maksmolchdmitr/EmemeBot");
        linkDao.findAll()
                .forEach(link ->{
                    assertEquals(link.url(), "https://github.com/maksmolchdmitr/EmemeBot");
                    assertNull(link.lastUpdate());
                });
    }

    @Test
    @Transactional
    @Rollback
    void remove() {
        linkDao.add(new Link("https://github.com/maksmolchdmitr/EmemeBot",
                new Timestamp(0)));
        linkDao.remove("https://github.com/maksmolchdmitr/EmemeBot");
        linkDao.findAll()
                .forEach(link ->{
                    throw new RuntimeException("Link table is not empty but it actually must be empty!");
                });
    }

    @Test
    @Transactional
    @Rollback
    void findAll() {
        linkDao.add(new Link("https://github.com/maksmolchdmitr/EmemeBot",
                new Timestamp(0)));
        linkDao.add(new Link("https://github.com/maksmolchdmitr/tinkoffLinkUpdater",
                new Timestamp(0)));
        linkDao.add(new Link("https://stackoverflow.com/questions/43569781/unable-to-start-docker-service-with-error-failed-to-start-docker-service-unit",
                new Timestamp(0)));
        int countLinks = 0;
        for(Link ignored :linkDao.findAll()){
            countLinks++;
        }
        assertEquals(countLinks, 3);
    }

    @Test
    @Transactional
    @Rollback
    void removeIfWithOneUser(){
        userDao.add(new User(0));
        userDao.add(new User(1));
        userDao.add(new User(2));
        linkDao.add(new Link("link"));
        linkDao.add(new Link("link2"));
        userLinksDao.add(new UserLinks(0, "link"));
        userLinksDao.add(new UserLinks(0, "link2"));
        userLinksDao.add(new UserLinks(1, "link2"));
        userLinksDao.add(new UserLinks(2, "link2"));
        linkDao.removeIfWithOneUser("link");
        assertEquals(linkDao.findAll().size(), 1);
    }
    @Test
    @Transactional
    @Rollback
    void removeIfWithTwoUser(){
        userDao.add(new User(0));
        userDao.add(new User(1));
        linkDao.add(new Link("link"));
        userLinksDao.add(new UserLinks(0, "link"));
        userLinksDao.add(new UserLinks(1, "link"));
        linkDao.removeIfWithOneUser("link");
        assertEquals(linkDao.findAll().size(), 1);
    }

    @Test
    @Transactional
    @Rollback
    void removeIfWithOneUserThree(){
        userDao.add(new User(0));
        userDao.add(new User(1));
        userDao.add(new User(2));
        linkDao.add(new Link("link1"));
        linkDao.add(new Link("link2"));
        linkDao.add(new Link("link3"));
        userLinksDao.add(new UserLinks(0, "link1"));
        userLinksDao.add(new UserLinks(1, "link1"));
        userLinksDao.add(new UserLinks(2, "link1"));
        userLinksDao.add(new UserLinks(0, "link2"));
        userLinksDao.add(new UserLinks(0, "link3"));
        linkDao.removeIfWithOneUser("link3");
        assertTrue(linkDao.findByUrl("link3").isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void updateLinkTest(){
        linkDao.add(new Link("link", new Timestamp(0)));
        linkDao.update(new Link("link", new Timestamp(1)));
        assertEquals(linkDao.findByUrl("link")
                .orElseThrow()
                .lastUpdate(),
                new Timestamp(1));
    }

    @Test
    @Transactional
    @Rollback
    void getSortedListOfLinksTest(){
        Link link4 = linkDao.add(new Link("link4", new Timestamp(3)));
        Link link2 = linkDao.add(new Link("link2", new Timestamp(1)));
        Link link5 = linkDao.add(new Link("link5", new Timestamp(4)));
        Link link3 = linkDao.add(new Link("link3", new Timestamp(2)));
        Link link1 = linkDao.add(new Link("link1", new Timestamp(0)));
        Link link = linkDao.add(new Link("link"));
        assertArrayEquals(linkDao.findAllSortedByLastUpdate().toArray(),
                new Object[]{
                        link1,
                        link2,
                        link3,
                        link4,
                        link5,
                        link
                });
    }
}