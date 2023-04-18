package org.example.jooq;

import org.example.model.GithubLink;
import org.example.model.Link;
import org.example.model.UserLinks;
import org.example.service.UserLinksService;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static org.example.jooq.Tables.*;

@Service
public class UserLinksServiceJooq implements UserLinksService {
    private final DSLContext DSL;

    public UserLinksServiceJooq(DSLContext dsl) {
        DSL = dsl;
    }

    @Override
    public UserLinks add(long chatId, String url) {
        DSL.insertInto(USER_LINKS_TABLE)
                .columns(USER_LINKS_TABLE.LINK_URL, USER_LINKS_TABLE.USER_CHAT_ID)
                .values(url, (int) chatId)
                .onConflictDoNothing()
                .execute();
        return DSL.selectFrom(USER_LINKS_TABLE)
                .where(USER_LINKS_TABLE.LINK_URL.eq(url)
                        .and(USER_LINKS_TABLE.USER_CHAT_ID.eq((int) chatId)))
                .fetchAnyInto(UserLinks.class);
    }

    @Override
    public UserLinks remove(long chatId, String url) {
        UserLinks res = DSL.selectFrom(USER_LINKS_TABLE)
                .where(USER_LINKS_TABLE.LINK_URL.eq(url)
                        .and(USER_LINKS_TABLE.USER_CHAT_ID.eq((int) chatId)))
                .fetchAnyInto(UserLinks.class);
        DSL.deleteFrom(USER_LINKS_TABLE)
                .where(USER_LINKS_TABLE.LINK_URL.eq(url)
                        .and(USER_LINKS_TABLE.USER_CHAT_ID.eq((int) chatId)))
                .execute();
        return res;
    }

    @Override
    public void removeLinkByUrl(String url) {
        DSL.deleteFrom(LINK_TABLE)
                .where(LINK_TABLE.URL.eq(url))
                .execute();
    }

    @Override
    public List<UserLinks> findByChatId(long chatId) {
        return DSL.selectFrom(USER_LINKS_TABLE)
                .where(USER_LINKS_TABLE.USER_CHAT_ID.eq((int) chatId))
                .fetchInto(UserLinks.class);
    }

    @Override
    public List<UserLinks> findByUrl(String url) {
        return DSL.selectFrom(USER_LINKS_TABLE)
                .where(USER_LINKS_TABLE.LINK_URL.eq(url))
                .fetchInto(UserLinks.class);
    }

    @Override
    public List<Link> findLinksSortedByLastUpdate() {
        return DSL.selectFrom(LINK_TABLE)
                .orderBy(LINK_TABLE.LAST_UPDATE)
                .fetchInto(Link.class);
    }

    @Override
    public int getBranchCount(String url) {
        return Objects.requireNonNull(DSL.selectFrom(GITHUB_LINK_TABLE)
                        .where(GITHUB_LINK_TABLE.LINK_URL.eq(url))
                        .fetchAnyInto(GithubLink.class))
                .branchCount();
    }

    @Override
    public boolean isGithubLink(String url) {
        return Objects.requireNonNull(DSL.selectFrom(LINK_TABLE)
                        .where(LINK_TABLE.URL.eq(url))
                        .fetchAnyInto(Link.class))
                .isGithubLink();
    }

    @Override
    public List<Link> findAllLinks() {
        return DSL.selectFrom(LINK_TABLE)
                .fetchInto(Link.class);
    }

    @Override
    public void updateLink(Link link) {
        DSL.update(LINK_TABLE)
                .set(LINK_TABLE.LAST_UPDATE, link.lastUpdate().toLocalDateTime())
                .set(LINK_TABLE.IS_GITHUB_LINK, link.isGithubLink())
                .where(LINK_TABLE.URL.eq(link.url()))
                .execute();
    }

    @Override
    public void setGithubLinkBranchCount(String url, int branchCount) {
        DSL.update(GITHUB_LINK_TABLE)
                .set(GITHUB_LINK_TABLE.BRANCH_COUNT, branchCount)
                .where(GITHUB_LINK_TABLE.LINK_URL.eq(url))
                .execute();
    }
}
