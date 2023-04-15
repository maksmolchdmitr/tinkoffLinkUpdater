package org.example;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.GithubRepositoryResponse;
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
        log.info("Update links...");
        userLinksService.findLinksSortedByLastUpdate()
                .forEach(this::handleLink);
    }

    private void handleLink(Link link) {
        log.info("Handle link %s".formatted(link.url()));
        URL url = getLinkUrl(link.url()).orElseThrow();
        getOldTime(link).ifPresentOrElse(
                oldTime -> userLinksService.updateLink(new Link(link.url(),
                        getAndSendMessageWithNewTime(url, oldTime))),
                ()->userLinksService.updateLink(new Link(link.url(), new Timestamp(System.currentTimeMillis()))));
    }

    private void sendMessage(String url, @NotNull Timestamp oldTime, @NotNull Timestamp newTime, String extraMessage) {
        log.info("send message with old time: \n%s and new time: \n%s"
                .formatted(oldTime, newTime));
        if(!newTime.after(oldTime)) return;
        botHttpClient.sendUpdates(new UpdateResponse(
                0,
                url,
                """
                        link was detected as updated link!
                        %s
                        """.formatted(extraMessage),
                userLinksService.findByUrl(url).stream()
                        .map(UserLinks::userChatId).collect(Collectors.toList())
                ));
    }

    private Timestamp getAndSendMessageWithNewTime(URL url, Timestamp oldTime) {
        Timestamp newTime = new Timestamp(System.currentTimeMillis());
        switch (UrlParser.parse(url)){
            case GithubLinkParser.GithubData githubData ->{
                GithubRepositoryResponse.Event event = githubClient.getEvents(githubData.getUserAndRepository().user(),
                        githubData.getUserAndRepository().repository())
                        [0];
                newTime = event.createdAt();
                int branchCount = githubClient.getBranches(githubData.getUserAndRepository().user(),
                        githubData.getUserAndRepository().repository()).length;
                if(branchCount>userLinksService.getBranchCount(url.toString())){
                    userLinksService.setGithubLinkBranchCount(url.toString(), branchCount);
                    newTime = new Timestamp(System.currentTimeMillis());
                    String branchName = githubClient.getBranches(githubData.getUserAndRepository().user(),
                            githubData.getUserAndRepository().repository())[0].name();
                    sendMessage(url.toString(), oldTime, newTime, """
                        new branch was added with name %s
                        """.formatted(branchName));
                }else {
                    sendMessage(url.toString(), oldTime, newTime, """
                        new some event %s
                        """.formatted(event.toString()));
                }
            }
            case StackoverflowLinkParser.StackoverflowData stackoverflowData ->{
                newTime = Timestamp.from(stackoverflowClient.getAnswer(stackoverflowData.getQuestionId())
                        .answers().get(0).lastActivityDate().toInstant());
                sendMessage(url.toString(), oldTime, newTime, "(-(._.)-)");
            }
            case UrlParser.EmptyData ignored -> {}
        }
        return newTime;
    }

    private Optional<Timestamp> getOldTime(Link link) {
        return Optional.ofNullable(link.lastUpdate());
    }

    private Optional<URL> getLinkUrl(String url) {
        try{
            return Optional.of(new URL(url));
        }catch (MalformedURLException e){
            return Optional.empty();
        }
    }
}
