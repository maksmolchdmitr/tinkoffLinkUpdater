/*
 * This file is generated by jOOQ.
 */
package org.example.jooq.tables;


import java.util.function.Function;

import javax.annotation.processing.Generated;

import org.example.jooq.DefaultSchema;
import org.example.jooq.Keys;
import org.example.jooq.tables.records.UserTableRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function2;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row2;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


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
public class UserTable extends TableImpl<UserTableRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>USER_TABLE</code>
     */
    public static final UserTable USER_TABLE = new UserTable();

    /**
     * The class holding records for this type
     */
    @Override
    @NotNull
    public Class<UserTableRecord> getRecordType() {
        return UserTableRecord.class;
    }

    /**
     * The column <code>USER_TABLE.CHAT_ID</code>.
     */
    public final TableField<UserTableRecord, Integer> CHAT_ID = createField(DSL.name("CHAT_ID"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>USER_TABLE.USERNAME</code>.
     */
    public final TableField<UserTableRecord, String> USERNAME = createField(DSL.name("USERNAME"), SQLDataType.VARCHAR(32), this, "");

    private UserTable(Name alias, Table<UserTableRecord> aliased) {
        this(alias, aliased, null);
    }

    private UserTable(Name alias, Table<UserTableRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>USER_TABLE</code> table reference
     */
    public UserTable(String alias) {
        this(DSL.name(alias), USER_TABLE);
    }

    /**
     * Create an aliased <code>USER_TABLE</code> table reference
     */
    public UserTable(Name alias) {
        this(alias, USER_TABLE);
    }

    /**
     * Create a <code>USER_TABLE</code> table reference
     */
    public UserTable() {
        this(DSL.name("USER_TABLE"), null);
    }

    public <O extends Record> UserTable(Table<O> child, ForeignKey<O, UserTableRecord> key) {
        super(child, key, USER_TABLE);
    }

    @Override
    @NotNull
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    @NotNull
    public UniqueKey<UserTableRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_C;
    }

    @Override
    @NotNull
    public UserTable as(String alias) {
        return new UserTable(DSL.name(alias), this);
    }

    @Override
    @NotNull
    public UserTable as(Name alias) {
        return new UserTable(alias, this);
    }

    @Override
    @NotNull
    public UserTable as(Table<?> alias) {
        return new UserTable(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public UserTable rename(String name) {
        return new UserTable(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public UserTable rename(Name name) {
        return new UserTable(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public UserTable rename(Table<?> name) {
        return new UserTable(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row2<Integer, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function2<? super Integer, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function2<? super Integer, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}