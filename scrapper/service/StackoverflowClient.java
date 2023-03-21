package service;

import dto.StackoverflowQuestionsResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url = "/2.3/questions/{id}?site=stackoverflow")
public interface StackoverflowClient {
    @GetExchange
    StackoverflowQuestionsResponse getQuestion(@PathVariable String id);
}
