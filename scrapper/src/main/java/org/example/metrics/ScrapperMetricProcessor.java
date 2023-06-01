package org.example.metrics;

import io.micrometer.core.instrument.Metrics;

public final class ScrapperMetricProcessor {
    private final static String HANDLED_LINK_COUNT_NAME = "handled_link_count";

    public static void incrementHandledLinkCount() {
        Metrics.counter(HANDLED_LINK_COUNT_NAME).increment();
    }
}
