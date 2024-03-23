package com.feige.utils.micrometer;

import java.time.Duration;

/**
 * {@link ConfigurationProperties} for configuring JMX metrics export.
 *
 * @author Jon Schneider
 */
public class JmxProperties {
    /**
     * Metrics JMX domain name.
     */
    private String domain = "metrics";

    /**
     * Step size (i.e. reporting frequency) to use.
     */
    private Duration step = Duration.ofMinutes(1);

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Duration getStep() {
        return this.step;
    }

    public void setStep(Duration step) {
        this.step = step;
    }
}
