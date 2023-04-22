package org.example.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "link_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkEntity {
    @Id
    @Column(name = "url")
    private String url;

    @Column(name = "last_update")
    private Timestamp lastUpdate;

    @Column(name = "is_github_link")
    private Boolean isGithubLink;

    @ManyToMany
    @JoinTable(name = "user_links_table",
            joinColumns = @JoinColumn(name = "link_url"),
            inverseJoinColumns = @JoinColumn(name = "user_chat_id"))
    private Set<User> users = new HashSet<>();
}
