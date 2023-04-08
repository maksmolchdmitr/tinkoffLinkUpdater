package org.example.dao;

import org.example.model.UserLinks;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserLinksDao {
    public UserLinksDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<UserLinks> userLinksRowMapper = new DataClassRowMapper<>(UserLinks.class);
    public void add(UserLinks userLinks){
        jdbcTemplate.update("insert into user_links_table(user_chat_id, link_id) values(:userChatId, :linkId)",
                new BeanPropertySqlParameterSource(userLinks));
    }
    public void removeByUserChatId(long userChatId){
        jdbcTemplate.update("delete from user_links_table where user_chat_id=:userChatId",
                Map.of("userChatId", userChatId));
    }

    public void removeByLinkId(long linkId){
        jdbcTemplate.update("delete from user_links_table where link_id=:linkId",
                Map.of("linkId", linkId));
    }
    public Iterable<UserLinks> findAll(){
        return jdbcTemplate.query("select * from user_links_table", userLinksRowMapper);
    }
}
