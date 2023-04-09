package org.example.dao;

import org.example.IntegrationEnvironment;
import org.example.model.Link;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class LinkDaoTest extends IntegrationEnvironment {
    @Autowired
    private LinkDao linkDao;

    @Test
    @Transactional
    @Rollback
    void add() {
        Link newLink = linkDao.add(new Link("https://github.com/maksmolchdmitr/EmemeBot"));
        assertEquals(newLink.id(), 0);
        assertNull(newLink.lastUpdate());
        assertEquals(newLink.url(), "https://github.com/maksmolchdmitr/EmemeBot");
        linkDao.findAll()
                .forEach(link ->{
                    assertEquals(link.url(), "https://github.com/maksmolchdmitr/EmemeBot");
                    assertEquals(link.id(), 0);
                    assertNull(link.lastUpdate());
                });
    }

    @Test
    @Transactional
    @Rollback
    void remove() {
        Link newLink = linkDao.add(new Link(0, "https://github.com/maksmolchdmitr/EmemeBot",
                new Timestamp(0)));
        Link removedLink = linkDao.remove(0);
        assertEquals(newLink, removedLink);
        linkDao.findAll()
                .forEach(link ->{
                    throw new RuntimeException("Link table is not empty but it actually must be empty!");
                });
    }

    @Test
    @Transactional
    @Rollback
    void findAll() {
        linkDao.add(new Link(0, "https://github.com/maksmolchdmitr/EmemeBot",
                new Timestamp(0)));
        linkDao.add(new Link(1, "https://github.com/maksmolchdmitr/tinkoffLinkUpdater",
                new Timestamp(0)));
        linkDao.add(new Link(2, "https://stackoverflow.com/questions/43569781/unable-to-start-docker-service-with-error-failed-to-start-docker-service-unit",
                new Timestamp(0)));
        int countLinks = 0;
        for(Link ignored :linkDao.findAll()){
            countLinks++;
        }
        assertEquals(countLinks, 3);
    }
}