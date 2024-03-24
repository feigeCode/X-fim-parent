package com.feige.utils.micrometer;

import java.time.Duration;

/**
 * configuring metrics export to Prometheus.
 */
public class PrometheusProperties {
    /**
     * Whether to enable publishing descriptions as part of the scrape payload to
     * Prometheus. Turn this off to minimize the amount of data sent on each scrape.
     */
    private boolean descriptions = true;

    /**
     * Step size (i.e. reporting frequency) to use.
     */
    private Duration step = Duration.ofMinutes(1);

    public boolean isDescriptions() {
        return this.descriptions;
    }

    public void setDescriptions(boolean descriptions) {
        this.descriptions = descriptions;
    }

    public Duration getStep() {
        return this.step;
    }

    public void setStep(Duration step) {
        this.step = step;
    }

}
