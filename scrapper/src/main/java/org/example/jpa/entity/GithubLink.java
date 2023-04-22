package org.example.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "github_link_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubLink {
    @Id
    @Column(name = "link_url")
    private String linkUrl;

    @Column(name = "branch_count")
    private int branchCount;

    @ManyToOne
    @JoinColumn(name = "link_url", referencedColumnName = "url", insertable = false, updatable = false)
    private LinkEntity link;
}
