package org.example;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.UpdateResponse;
import org.example.model.Link;
import org.example.model.UserLinks;
import org.example.realization.GithubLinkParser;
import org.example.realization.StackoverflowLinkParser;
import org.example.realization.UrlParser;
import org.example.service.BotHttpClient;
import org.example.service.GithubClient;
import org.example.service.StackoverflowClient;
import org.example.service.UserLinksService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LinkUpdaterScheduler {
    private final UserLinksService userLinksService;
    private final GithubClient githubClient;
    private final StackoverflowClient stackoverflowClient;
    private final BotHttpClient botHttpClient;
    public LinkUpdaterScheduler(UserLinksService userLinksService, GithubClient githubClient, StackoverflowClient stackoverflowClient, BotHttpClient botHttpClient) {
        this.userLinksService = userLinksService;
        this.githubClient = githubClient;
        this.stackoverflowClient = stackoverflowClient;
        this.botHttpClient = botHttpClient;
    }
    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void update(){
        userLinksService.findLinksSortedByLastUpdate()
                .forEach(this::handleLink);
    }

    private void handleLink(Link link) {
        log.info("handle link %s".formatted(link.url()));
        Optional<URL> optionalURL = getLinkUrl(link.url());
        optionalURL.ifPresentOrElse(
                url ->{
                    Optional<Timestamp> oldTimeOptional = getOldTime(link);
                    Timestamp newTime = getNewTime(url);
                    userLinksService.updateLink(new Link(link.url(), newTime));
                    oldTimeOptional.ifPresent(
                            oldTime -> sendMessage(link.url(), oldTime, newTime));
                },
                () -> sendErrorMessageAndRemoveLink(link)
        );
    }

    private void sendMessage(String url, @NotNull Timestamp oldTime, @NotNull Timestamp newTime) {
        log.info("send message with old time: \n%s and new time: \n%s"
                .formatted(oldTime, newTime));
        if(!newTime.after(oldTime)) return;
        botHttpClient.sendUpdates(new UpdateResponse(
                0,
                url,
                """
                        link was detected as updated link!
                        """,
                userLinksService.findByUrl(url).stream()
                        .map(UserLinks::userChatId).collect(Collectors.toList())
                ));
    }

    private Optional<Timestamp> getOldTime(Link link) {
        return Optional.ofNullable(link.lastUpdate());
    }

    private Timestamp getNewTime(URL url) {
        switch (UrlParser.parse(url)){
            case GithubLinkParser.GithubData githubData ->{
                return githubClient.getEvents(githubData.getUserAndRepository().user(),
                        githubData.getUserAndRepository().repository())
                        [0].createdAt();
            }
            case StackoverflowLinkParser.StackoverflowData stackoverflowData ->{
                return Timestamp.from(stackoverflowClient.getQuestion(stackoverflowData.getQuestionId())
                        .items().get(0).lastActivityDate().toInstant());
            }
            case UrlParser.EmptyData ignored -> {}
        }
        return new Timestamp(System.currentTimeMillis());
    }

    private Optional<URL> getLinkUrl(String url) {
        try{
            return Optional.of(new URL(url));
        }catch (MalformedURLException e){
            return Optional.empty();
        }
    }

    private void sendErrorMessageAndRemoveLink(Link link) {
        botHttpClient.sendUpdates(new UpdateResponse(
                0,
                link.url(),
                """
                        It's not a link therefore it has been deleted!
                        """,
                userLinksService.findByUrl(link.url())
                        .stream().map(UserLinks::userChatId)
                        .collect(Collectors.toList())
        ));
        userLinksService.removeLinkByUrl(link.url());
    }
}
