package org.example.jpa.repository;

import org.example.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query(value = "insert into user_table (chat_id, username) values (:chatId, :username) on conflict do nothing",
            nativeQuery = true)
    void saveOnConflictDoNothing(@Param("chatId") Long chatId, @Param("username") String username);

    @Query(value = "select * from user_links_table where user_chat_id=:chatId", nativeQuery = true)
    List<String> findAllLinks(@Param("chatId") Long chatId);
}
