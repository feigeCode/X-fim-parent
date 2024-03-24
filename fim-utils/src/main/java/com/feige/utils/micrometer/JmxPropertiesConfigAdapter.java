package com.feige.utils.micrometer;

import io.micrometer.jmx.JmxConfig;

import java.time.Duration;

/**
 * Adapter to convert {@link JmxProperties} to a {@link JmxConfig}.
 *
 */
public class JmxPropertiesConfigAdapter extends PropertiesConfigAdapter<JmxProperties> implements JmxConfig {

    JmxPropertiesConfigAdapter(JmxProperties properties) {
        super(properties);
    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public String domain() {
        return get(JmxProperties::getDomain, JmxConfig.super::domain);
    }

    @Override
    public Duration step() {
        return get(JmxProperties::getStep, JmxConfig.super::step);
    }
}
