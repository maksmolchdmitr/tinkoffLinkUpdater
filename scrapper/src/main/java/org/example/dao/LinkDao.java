package org.example.dao;

import org.example.model.Link;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class LinkDao {
    public LinkDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<Link> linkRowMapper = (rs, rowNum) -> new Link(rs.getString("url"),
            rs.getTimestamp("last_update"));
    public Link add(Link link){
        jdbcTemplate.update("""
                insert into link_table(url, last_update)
                values (:url, :lastUpdate)
                on conflict do nothing
                """, new BeanPropertySqlParameterSource(link));
        return jdbcTemplate.queryForObject(
                "select * from link_table where url=:url",
                new BeanPropertySqlParameterSource(link),
                linkRowMapper);
    }

    public void remove(String url){
        jdbcTemplate.update("delete from link_table where url=:url",
                Map.of("url", url));
    }
    public void removeIfWithOneUser(String url){
        jdbcTemplate.update("""
        delete from link_table where url=:url
        and (
            select count(*) from user_links_table
            where link_url=:url
        )=1
        """, Map.of("url", url));
    }
    public void update(Link link){
        jdbcTemplate.update("""
                update link_table
                set last_update=:lastUpdate
                where url=:url
                """, new BeanPropertySqlParameterSource(link));
    }
    public List<Link> findAll(){
        return jdbcTemplate.query("select * from link_table", linkRowMapper);
    }
    public Optional<Link> findByUrl(String url){
        return Optional.ofNullable(
                DataAccessUtils.singleResult(
                        jdbcTemplate.query("select * from link_table where url=:url",
                                Map.of("url", url),
                                linkRowMapper)
                )
        );
    }
}
