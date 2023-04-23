package org.example.service;

import org.example.dto.UpdateResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(url = "/updates")
public interface BotHttpClient {
    @PostExchange
    void sendUpdates(@RequestBody UpdateResponse updateResponse);
}
