import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import service.GithubClient;
import service.StackoverflowClient;

@Configuration
public class ClientConfiguration {
    @Bean
    GithubClient getGithubClient(){
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder()
                .clientAdapter(WebClientAdapter.forClient(
                        WebClient.builder()
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
}
