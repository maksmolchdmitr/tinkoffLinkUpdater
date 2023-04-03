package org.example.service;

import org.example.dto.GithubRepositoryResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url = "repos/{owner}/{repo}", accept = "application/vnd.github+json")
public interface GithubClient {
    @GetExchange
    GithubRepositoryResponse getRepository(@PathVariable String owner, @PathVariable String repo);
}
