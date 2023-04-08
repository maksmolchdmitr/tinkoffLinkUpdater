package org.example.dao;

import org.example.model.User;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    public UserDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private final RowMapper<User> userRowMapper = new DataClassRowMapper<>(User.class);
    public void add(User user){
        jdbcTemplate.update("insert into user_table(chat_id, username) values(:chatId, :username)",
                new BeanPropertySqlParameterSource(user));
    }
    public void remove(long chatId){
        jdbcTemplate.update("delete from user_table where chat_id=:chatId",
                Map.of("chatId", chatId));
    }
    public Iterable<User> findAll(){
        return jdbcTemplate.query("select * from user_table", userRowMapper);
    }
}
