package org.example.dao;

import org.example.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class UserDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    public UserDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private final RowMapper<User> userRowMapper =
            (rs, rowNum) -> new User(rs.getLong("chat_id"), rs.getString("username"));

    public User add(User user){
        jdbcTemplate.update("""
                insert into user_table(chat_id, username)
                values(:chatId, :username)
                on conflict do nothing
                """, new BeanPropertySqlParameterSource(user));
        return jdbcTemplate.queryForObject("select * from user_table where chat_id=:chatId",
                Map.of("chatId", user.chatId()),
                userRowMapper);
    }
    public User remove(long chatId){
        return jdbcTemplate.queryForObject("delete from user_table where chat_id=:chatId returning *",
                Map.of("chatId", chatId),
                userRowMapper);
    }
    public List<User> findAll(){
        return jdbcTemplate.query("select * from user_table", userRowMapper);
    }
}
