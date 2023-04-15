package org.example.dao;

import org.example.model.GithubLink;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GithubLinkDao {
    public GithubLinkDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<GithubLink> githubLinkRowMapper =
            (rs, rowNum) -> new GithubLink(rs.getString("link_url"),
                    rs.getInt("branch_count"));
    public GithubLink add(GithubLink githubLink){
        jdbcTemplate.update("""
                insert into github_link_table(link_url, branch_count)
                values (:linkUrl, :branchCount)
                on conflict do nothing
                """, new BeanPropertySqlParameterSource(githubLink));
        return jdbcTemplate.queryForObject(
                "select * from github_link_table where link_url=:linkUrl",
                new BeanPropertySqlParameterSource(githubLink),
                githubLinkRowMapper);
    }
    public void update(GithubLink githubLink){
        jdbcTemplate.update("""
                update github_link_table
                set branch_count=:branchCount
                where link_url=:linkUrl
                """, new BeanPropertySqlParameterSource(githubLink));
    }
    public GithubLink findByUrl(String url){
        return jdbcTemplate.queryForObject(
                "select * from github_link_table where link_url=:linkUrl",
                Map.of("linkUrl", url),
                githubLinkRowMapper);
    }
}
