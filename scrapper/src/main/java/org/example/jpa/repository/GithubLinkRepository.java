package org.example.jpa.repository;

import org.example.jpa.entity.GithubLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubLinkRepository extends JpaRepository<GithubLink, String> {
}
