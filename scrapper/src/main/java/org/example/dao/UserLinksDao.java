package org.example.dao;

import org.example.model.UserLinks;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class UserLinksDao {
    public UserLinksDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<UserLinks> userLinksRowMapper = new DataClassRowMapper<>(UserLinks.class);
    public Optional<UserLinks> add(UserLinks userLinks){
        jdbcTemplate.update("""
                insert into user_links_table(user_chat_id, link_url)
                select :userChatId, :linkUrl
                where exists(
                    select 1 from user_table
                    where chat_id=:userChatId
                )
                """, new BeanPropertySqlParameterSource(userLinks));
        return Optional.ofNullable(
                DataAccessUtils.singleResult(
                        jdbcTemplate.query("select * from user_links_table where user_chat_id=:userChatId and link_url=:linkUrl",
                                new BeanPropertySqlParameterSource(userLinks),
                                userLinksRowMapper)
                )
        );
    }
    public UserLinks remove(UserLinks userLinks){
        return jdbcTemplate.queryForObject("delete from user_links_table where user_chat_id=:userChatId and link_url=:linkUrl returning *",
                new BeanPropertySqlParameterSource(userLinks),
                userLinksRowMapper);
    }
    public UserLinks removeWithLink(UserLinks userLinks){
        UserLinks res = jdbcTemplate.queryForObject("""
                select * from user_links_table
                where user_chat_id=:userChatId
                and link_url=:linkUrl
                """, new BeanPropertySqlParameterSource(userLinks),
                userLinksRowMapper);
        jdbcTemplate.update("""
                delete from link_table where url=:url
                and (
                    select count(*) from user_links_table
                    where link_url=:url
                )=1
                """, Map.of("url", userLinks.linkUrl()));
        jdbcTemplate.update("""
                delete from user_links_table
                where user_chat_id=:userChatId
                and link_url=:linkUrl
                """, new BeanPropertySqlParameterSource(userLinks));
        return res;
    }
    public void removeByUserChatId(long userChatId){
        jdbcTemplate.update("""
                delete from user_links_table where user_chat_id=:userChatId
                """,
                Map.of("userChatId", userChatId));
    }

    public void removeByLinkUrl(String url){
        jdbcTemplate.update("delete from user_links_table where link_url=:url",
                Map.of("url", url));
    }
    public List<UserLinks> findAll(){
        return jdbcTemplate.query("select * from user_links_table", userLinksRowMapper);
    }
    public List<UserLinks> findByChatId(long chatId){
        return jdbcTemplate.query("select * from user_links_table where user_chat_id=:chatId",
                Map.of("chatId", chatId), userLinksRowMapper);
    }
}
