package org.example.dao;

import org.example.IntegrationEnvironment;
import org.example.model.GithubLink;
import org.example.model.Link;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GithubLinkDaoTest extends IntegrationEnvironment {
    @Autowired
    private GithubLinkDao githubLinkDao;
    @Autowired
    private LinkDao linkDao;
    @Test
    @Transactional
    @Rollback
    void addTest() {
        linkDao.add(new Link("link"));
        int branchCount = 2193;
        githubLinkDao.add(new GithubLink("link", branchCount));
        GithubLink githubLink = githubLinkDao.findByUrl("link");
        assertEquals(githubLink.branchCount(), branchCount);
    }

    @Test
    @Transactional
    @Rollback
    void updateTest() {
        linkDao.add(new Link("link"));
        int branchCount = 2193;
        githubLinkDao.add(new GithubLink("link", branchCount));
        GithubLink githubLink = githubLinkDao.findByUrl("link");
        assertEquals(githubLink.branchCount(), branchCount);
        int newBranchCount = 2348923;
        githubLinkDao.update(new GithubLink("link", newBranchCount));
        assertEquals(githubLinkDao.findByUrl("link").branchCount(),
                newBranchCount);
    }
}