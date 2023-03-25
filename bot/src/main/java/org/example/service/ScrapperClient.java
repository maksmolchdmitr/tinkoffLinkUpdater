package org.example.service;

import org.example.dto.AddLinkRequest;
import org.example.dto.LinkResponse;
import org.example.dto.ListLinksResponse;
import org.example.dto.RemoveLinkRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(url = "localhost:8080")
public interface ScrapperClient {
    @PostExchange("/tg-chat/{id}")
    void registerUser(@PathVariable String id);
    @DeleteExchange("/tg-chat/{id}")
    void deleteUser(@PathVariable String id);

    @GetExchange("links")
    ListLinksResponse getLinks();
    @PostExchange("links")
    void addLink(@RequestBody AddLinkRequest addLinkRequest);
    @DeleteExchange("links")
    LinkResponse deleteLink(@RequestBody RemoveLinkRequest removeLinkRequest);
}
