package org.example.jooq;

import org.example.service.TelegramChatService;
import org.jooq.DSLContext;

import static org.example.jooq.Tables.USER_TABLE;

public class TelegramChatServiceJooq implements TelegramChatService {
    private final DSLContext DSL;

    public TelegramChatServiceJooq(DSLContext dsl) {
        DSL = dsl;
    }

    @Override
    public void register(long chatId) {
        DSL.insertInto(USER_TABLE)
                .columns(USER_TABLE.CHAT_ID)
                .values((int) chatId)
                .onConflictDoNothing()
                .execute();
    }

    @Override
    public void unregister(long chatId) {
        DSL.deleteFrom(USER_TABLE)
                .where(USER_TABLE.CHAT_ID.eq((int) chatId))
                .execute();
    }
}
