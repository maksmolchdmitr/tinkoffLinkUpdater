package org.example.metrics;

import io.micrometer.core.instrument.Metrics;

public final class BotMetricProcessor {
    private final static String HANDLED_MESSAGE_COUNT_NAME = "handled_message_count";

    public static void incrementHandledMessageCount() {
        Metrics.counter(HANDLED_MESSAGE_COUNT_NAME).increment();
    }
}
