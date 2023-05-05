package org.example.jpa;

import org.example.jpa.entity.GithubLink;
import org.example.jpa.entity.LinkEntity;
import org.example.jpa.entity.User;
import org.example.jpa.repository.GithubLinkRepository;
import org.example.jpa.repository.LinkRepository;
import org.example.jpa.repository.UserRepository;
import org.example.model.Link;
import org.example.model.UserLinks;
import org.example.service.UserLinksService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class UserLinkServiceJpa implements UserLinksService {
    private final LinkRepository linkRepository;
    private final UserRepository userRepository;
    private final GithubLinkRepository githubLinkRepository;

    public UserLinkServiceJpa(
            LinkRepository linkRepository,
            UserRepository userRepository,
            GithubLinkRepository githubLinkRepository
    ) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
        this.githubLinkRepository = githubLinkRepository;
    }

    @Override
    @Transactional
    public UserLinks add(long chatId, String url) {
        userRepository.saveOnConflictDoNothing(chatId, null);
        User user = userRepository.findById(chatId).orElseThrow();

        linkRepository.saveOnConflictDoNothing(url, null, false);
        LinkEntity link = linkRepository.findById(url).orElseThrow();

        link.getUsers().add(user);
        linkRepository.saveAndFlush(link);
        return new UserLinks(user.getChatId(), link.getUrl());
    }

    @Override
    @Transactional
    public UserLinks remove(long chatId, String url) {
        User user = userRepository.findById(chatId).orElseThrow();
        LinkEntity link = linkRepository.findById(url).orElseThrow();
        link.getUsers().remove(user);
        linkRepository.saveAndFlush(link);
        return new UserLinks(user.getChatId(), link.getUrl());
    }

    @Override
    @Transactional
    public void removeLinkByUrl(String url) {
        linkRepository.deleteById(url);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserLinks> findByChatId(long chatId) {
        return userRepository.findAllLinks(chatId)
                .stream()
                .map(linkUrl -> new UserLinks(chatId, linkUrl))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserLinks> findByUrl(String url) {
        LinkEntity link = linkRepository.findById(url).orElseThrow();
        return link.getUsers().stream()
                .map(user -> new UserLinks(user.getChatId(), url))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Link> findLinksSortedByLastUpdate() {
        return linkRepository.findAllByOrderByLastUpdate()
                .stream()
                .map(linkEntity -> new Link(
                        linkEntity.getUrl(),
                        linkEntity.getLastUpdate(),
                        linkEntity.getIsGithubLink())
                ).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public int getBranchCount(String url) {
        return githubLinkRepository.findById(url)
                .orElse(new GithubLink(url, 0, null))
                .getBranchCount();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isGithubLink(String url) {
        return linkRepository.findById(url)
                .orElseThrow()
                .getIsGithubLink();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Link> findAllLinks() {
        return linkRepository.findAll()
                .stream()
                .map(linkEntity -> new Link(linkEntity.getUrl()))
                .toList();
    }

    @Override
    @Transactional
    public void updateLink(Link link) {
        LinkEntity linkEntity = new LinkEntity();
        linkEntity.setUrl(link.url());
        linkEntity.setLastUpdate(link.lastUpdate());
        if (link.isGithubLink() != null)
            linkEntity.setIsGithubLink(link.isGithubLink());
        linkRepository.save(linkEntity);
    }

    @Override
    @Transactional
    public void setGithubLinkBranchCount(String url, int branchCount) {
        GithubLink githubLink = new GithubLink();
        githubLink.setLinkUrl(url);
        githubLink.setBranchCount(branchCount);
        githubLinkRepository.save(githubLink);
    }
}
