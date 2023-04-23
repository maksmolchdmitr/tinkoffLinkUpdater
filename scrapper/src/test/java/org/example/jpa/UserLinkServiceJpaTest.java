package org.example.jpa;

import jakarta.transaction.Transactional;
import org.example.IntegrationEnvironment;
import org.example.model.Link;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.sql.Timestamp;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserLinkServiceJpaTest extends IntegrationEnvironment {
    @Autowired
    private UserLinkServiceJpa userLinkServiceJpa;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        userLinkServiceJpa.add(0, "link");
        assertEquals(userLinkServiceJpa.findAllLinks().size(), 1);
    }

    @Test
    @Transactional
    @Rollback
    void deleteTest() {
        userLinkServiceJpa.add(0, "link");
        assertEquals(userLinkServiceJpa.findByUrl("link").size(), 1);
        userLinkServiceJpa.remove(0, "link");
        assertEquals(userLinkServiceJpa.findByUrl("link").size(), 0);
    }

    @Test
    @Transactional
    @Rollback
    void moreComplicatedDeleteTest() {
        userLinkServiceJpa.add(0, "link");
        userLinkServiceJpa.add(1, "link");
        userLinkServiceJpa.add(2, "link");
        userLinkServiceJpa.remove(1, "link");
        assertEquals(userLinkServiceJpa.findByUrl("link").size(), 2);
        assertEquals(userLinkServiceJpa.findByChatId(0).size(), 1);
        assertEquals(userLinkServiceJpa.findByChatId(1).size(), 0);
        assertEquals(userLinkServiceJpa.findByChatId(2).size(), 1);
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        Arrays.asList("link", "link2", "link3").forEach(s -> userLinkServiceJpa.add(0, s));
        Arrays.asList("link", "link2", "link3").forEach(s -> userLinkServiceJpa.add(1, s));

        assertEquals(userLinkServiceJpa.findAllLinks().size(), 3);
        assertEquals(userLinkServiceJpa.findByChatId(0).size(), 3);
        assertEquals(userLinkServiceJpa.findByChatId(1).size(), 3);
        assertEquals(userLinkServiceJpa.findByUrl("link").size(), 2);
        assertEquals(userLinkServiceJpa.findByUrl("link2").size(), 2);
        assertEquals(userLinkServiceJpa.findByUrl("link3").size(), 2);
    }

    @Test
    @Transactional
    @Rollback
    void findAllSortedByLastUpdateTest(){
        Arrays.asList("link3", "link4", "link2", "link1", "link5", "link6").forEach(s -> userLinkServiceJpa.add(0, s));

        Link link1 = new Link("link1", new Timestamp(4), false);
        Link link2 = new Link("link2", new Timestamp(3), false);
        Link link3 = new Link("link3", new Timestamp(2), false);
        Link link4 = new Link("link4", new Timestamp(1), false);
        Link link5 = new Link("link5", new Timestamp(0), false);
        Link link6 = new Link("link6", false);
        Arrays.asList(link1, link2, link3, link4, link5).forEach(link -> userLinkServiceJpa.updateLink(link));

        System.out.println(userLinkServiceJpa.findLinksSortedByLastUpdate());
        assertArrayEquals(userLinkServiceJpa.findLinksSortedByLastUpdate().toArray(),
                new Object[]{
                        link5,
                        link4,
                        link3,
                        link2,
                        link1,
                        link6
                });
    }

    @Test
    @Transactional
    @Rollback
    void setGithubLinkBranchCountTest(){
        userLinkServiceJpa.add(0, "link");
        userLinkServiceJpa.setGithubLinkBranchCount("link", 10);
        assertEquals(userLinkServiceJpa.getBranchCount("link"), 10);
    }

}