package org.example.dao;

import org.example.model.User;
import org.springframework.jdbc.core.DataClassRowMapper;
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
    private final RowMapper<User> userRowMapper = new DataClassRowMapper<>(User.class);
    public User add(User user){
        return jdbcTemplate.queryForObject("insert into user_table(chat_id, username) values(:chatId, :username) returning *",
                new BeanPropertySqlParameterSource(user),
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
