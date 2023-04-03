package org.example.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.example.service.ScrapperClient;

@Configuration
public class ScrapperClientConfig {
    @Bean
    ScrapperClient getScrapperClient(){
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder()
                .clientAdapter(WebClientAdapter.forClient(
                        WebClient.builder().build()
                ))
                .build();
        return httpServiceProxyFactory.createClient(ScrapperClient.class);
    }
}
