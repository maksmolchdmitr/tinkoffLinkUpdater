package org.example;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.UpdateResponse;
import org.example.model.Link;
import org.example.model.UserLinks;
import org.example.realization.GithubLinkParser;
import org.example.realization.StackoverflowLinkParser;
import org.example.realization.UrlParser;
import org.example.service.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.example.dto.GithubRepositoryResponse.*;

@Slf4j
@Component
public class LinkUpdaterScheduler {
    private final UserLinksService userLinksService;
    private final GithubClient githubClient;
    private final StackoverflowClient stackoverflowClient;
    private final UpdateSender updateSender;
    public LinkUpdaterScheduler(UserLinksService userLinksService,
                                GithubClient githubClient, StackoverflowClient stackoverflowClient, UpdateSender updateSender) {
        this.userLinksService = userLinksService;
        this.githubClient = githubClient;
        this.stackoverflowClient = stackoverflowClient;
        this.updateSender = updateSender;
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
                        getAndSendMessageWithNewTime(url, oldTime)
                                .orElse(new Timestamp(System.currentTimeMillis())))),
                ()->userLinksService.updateLink(new Link(link.url(), new Timestamp(System.currentTimeMillis()))));
    }

    private void sendMessage(String url, @NotNull Timestamp oldTime, @NotNull Timestamp newTime, String extraMessage) {
        if(!newTime.after(oldTime)) return;
        updateSender.sendUpdates(new UpdateResponse(
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

    private Optional<Timestamp> getAndSendMessageWithNewTime(URL url, Timestamp oldTime) {
        switch (UrlParser.parse(url)){
            case GithubLinkParser.GithubData githubData -> {
                String userName = githubData.getUserAndRepository().user();
                String repoName = githubData.getUserAndRepository().repository();
                boolean newEvent = checkNewEvent(url, oldTime, userName, repoName);
                boolean newBranch = checkNewBranch(url, oldTime, userName, repoName);
                boolean newCommit = checkNewCommit(url, oldTime, userName, repoName);
                if(newCommit||newBranch||newEvent)
                    return Optional.of(new Timestamp(System.currentTimeMillis()));
            }
            case StackoverflowLinkParser.StackoverflowData stackoverflowData ->{
                Timestamp newTime = Timestamp.from(stackoverflowClient.getAnswer(stackoverflowData.getQuestionId())
                        .answers().get(0).lastActivityDate().toInstant());
                sendMessage(url.toString(), oldTime, newTime, "new answer to question!");
                return Optional.of(newTime);
            }
            case UrlParser.EmptyData ignored -> {}
        }
        return Optional.empty();
    }

    private boolean checkNewCommit(URL url, Timestamp oldTime, String userName, String repoName) {
        Timestamp newTime;
        CommitBody[] commitBodies =
                githubClient.getCommits(userName, repoName);
        newTime = Timestamp.from(commitBodies[0].commit().author().date().toInstant());
        sendMessage(url.toString(), oldTime, newTime, """
                new commit was created with name: "%s"
                """.formatted(commitBodies[0].commit().message()));
        return newTime.after(oldTime);
    }

    private boolean checkNewBranch(URL url, Timestamp oldTime, String userName, String repoName) {
        Branch[] branches =
                githubClient.getBranches(userName, repoName);
        if (branches.length > userLinksService.getBranchCount(url.toString())) {
            userLinksService.setGithubLinkBranchCount(url.toString(), branches.length);
            Timestamp newTime = new Timestamp(System.currentTimeMillis());
            sendMessage(url.toString(), oldTime,
                    newTime, """
                    new branch was added with branch names:
                    %s
                    """.formatted(Arrays.stream(branches)
                    .map(branch -> branch.name().formatted("\"%s\""))
                                    .reduce("%s\n%s"::formatted)
                                    .orElse("Empty list of branches")
                    )
            );
            return true;
        }
        return false;
    }

    private boolean checkNewEvent(URL url, Timestamp oldTime, String userName, String repoName) {
        Event lastEvent = githubClient.getEvents(userName, repoName)[0];
        Timestamp newTime = Timestamp.from(lastEvent.createdAt().toInstant());
        sendMessage(url.toString(), oldTime, newTime,
                "new some event %s".formatted(lastEvent.toString()));
        return newTime.after(oldTime);
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
