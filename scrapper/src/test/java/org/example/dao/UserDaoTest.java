package org.example.dao;

import org.example.IntegrationEnvironment;
import org.example.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDaoTest extends IntegrationEnvironment {
    @Autowired
    private UserDao userDao;
    @Test
    @Transactional
    @Rollback
    void add() {
        userDao.add(new User(0, "Maks"));
        userDao.findAll()
                .forEach(user -> assertEquals(user.username(), "Maks"));
    }

    @Test
    @Transactional
    @Rollback
    void remove() {
        userDao.add(new User(0, "Maks"));
        userDao.remove(0);
        userDao.findAll()
                .forEach(user -> {
                    throw  new RuntimeException("User table is not empty but it actually must be empty!");
                });
    }

    @Test
    @Transactional
    @Rollback
    void findAll() {
        userDao.add(new User(0, "Maks"));
        userDao.add(new User(1, "Kurita"));
        userDao.add(new User(2, "Lavandush"));
        int userCount = 0;
        for(User ignored :userDao.findAll()){
            userCount++;
        }
        assertEquals(userCount, 3);
    }
}