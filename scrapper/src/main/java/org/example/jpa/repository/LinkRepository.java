package org.example.jpa.repository;

import org.example.jpa.entity.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface LinkRepository extends JpaRepository<LinkEntity, String> {
    @Modifying
    @Query(value = "insert into link_table (url, last_update, is_github_link) values (:url, :lastUpdate, :isGithubLink) on conflict do nothing",
    nativeQuery = true)
    void saveOnConflictDoNothing(@Param("url") String url, @Param("lastUpdate")Timestamp lastUpdate, @Param("isGithubLink") boolean isGithubLink);

    List<LinkEntity> findAllByOrderByLastUpdate();
}
