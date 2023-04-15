package org.example.service;

import org.example.dto.StackoverflowAnswerResponse;
import org.example.dto.StackoverflowQuestionsResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url = "/2.3/questions/{id}")
public interface StackoverflowClient {
    @GetExchange("?site=stackoverflow")
    StackoverflowQuestionsResponse getQuestion(@PathVariable String id);
    @GetExchange("answers?site=stackoverflow")
    StackoverflowAnswerResponse getAnswer(@PathVariable String id);
}
