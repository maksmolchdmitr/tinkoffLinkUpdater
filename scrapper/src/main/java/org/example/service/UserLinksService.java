package org.example.service;

import org.example.model.Link;
import org.example.model.UserLinks;

import java.util.List;

public interface UserLinksService {
    UserLinks add(long chatId, String url);
    UserLinks remove(long chatId, String url);
    void removeLinkByUrl(String url);
    List<UserLinks> findByChatId(long chatId);
    List<UserLinks> findByUrl(String url);
    List<Link> findLinksSortedByLastUpdate();
    int getBranchCount(String url);
    boolean isGithubLink(String url);

    List<Link> findAllLinks();
    void updateLink(Link link);
    void setGithubLinkBranchCount(String url, int branchCount);
}
