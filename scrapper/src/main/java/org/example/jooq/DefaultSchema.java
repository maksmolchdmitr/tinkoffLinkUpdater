/*
 * This file is generated by jOOQ.
 */
package org.example.jooq;


import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.Generated;

import org.example.jooq.tables.GithubLinkTable;
import org.example.jooq.tables.LinkTable;
import org.example.jooq.tables.UserLinksTable;
import org.example.jooq.tables.UserTable;
import org.jetbrains.annotations.NotNull;
import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.17.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DefaultSchema extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>DEFAULT_SCHEMA</code>
     */
    public static final DefaultSchema DEFAULT_SCHEMA = new DefaultSchema();

    /**
     * The table <code>GITHUB_LINK_TABLE</code>.
     */
    public final GithubLinkTable GITHUB_LINK_TABLE = GithubLinkTable.GITHUB_LINK_TABLE;

    /**
     * The table <code>LINK_TABLE</code>.
     */
    public final LinkTable LINK_TABLE = LinkTable.LINK_TABLE;

    /**
     * The table <code>USER_LINKS_TABLE</code>.
     */
    public final UserLinksTable USER_LINKS_TABLE = UserLinksTable.USER_LINKS_TABLE;

    /**
     * The table <code>USER_TABLE</code>.
     */
    public final UserTable USER_TABLE = UserTable.USER_TABLE;

    /**
     * No further instances allowed
     */
    private DefaultSchema() {
        super("", null);
    }


    @Override
    @NotNull
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    @NotNull
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            GithubLinkTable.GITHUB_LINK_TABLE,
            LinkTable.LINK_TABLE,
            UserLinksTable.USER_LINKS_TABLE,
            UserTable.USER_TABLE
        );
    }
}
