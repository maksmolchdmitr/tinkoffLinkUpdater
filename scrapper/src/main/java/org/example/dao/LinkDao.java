package org.example.dao;

import org.example.model.Link;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class LinkDao {
    public LinkDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<Link> linkRowMapper = (rs, rowNum) -> new Link(rs.getInt("id"),
            rs.getString("url"),
            rs.getTimestamp("last_update"));
    public Link add(Link link){
        return jdbcTemplate.queryForObject("insert into link_table(id, url, last_update) values(:id, :url, :lastUpdate) returning *",
                new BeanPropertySqlParameterSource(link),
                linkRowMapper);
    }
    public Link remove(long id){
        return jdbcTemplate.queryForObject("delete from link_table where id=:id returning *", Map.of("id", id),
                linkRowMapper);
    }
    public List<Link> findAll(){
        return jdbcTemplate.query("select * from link_table", linkRowMapper);
    }
}
