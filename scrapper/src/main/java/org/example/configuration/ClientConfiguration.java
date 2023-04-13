package org.example.configuration;

import org.example.service.BotHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.example.service.GithubClient;
import org.example.service.StackoverflowClient;

@Configuration
public class ClientConfiguration {
    @Bean
    GithubClient getGithubClient(){
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder()
                .clientAdapter(WebClientAdapter.forClient(
                        WebClient.builder()
                                .codecs(configurer -> configurer.defaultCodecs()
                                        .maxInMemorySize(1 << 20))
                                .baseUrl("https://api.github.com/")
                                .build()
                ))
                .build();
        return httpServiceProxyFactory.createClient(GithubClient.class);
    }
    @Bean
    StackoverflowClient stackoverflowClient(){
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder()
                .clientAdapter(WebClientAdapter.forClient(
                        WebClient.builder()
                                .baseUrl("https://api.stackexchange.com")
                                .build()
                ))
                .build();
        return httpServiceProxyFactory.createClient(StackoverflowClient.class);
    }
    @Bean
    BotHttpClient botHttpClient(){
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder()
                .clientAdapter(WebClientAdapter.forClient(
                        WebClient.builder()
                                .baseUrl("http://localhost:8081/")
                                .build()
                ))
                .build();
        return httpServiceProxyFactory.createClient(BotHttpClient.class);
    }
}
