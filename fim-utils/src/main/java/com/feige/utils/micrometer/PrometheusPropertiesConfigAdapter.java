package com.feige.utils.micrometer;

import io.micrometer.prometheus.PrometheusConfig;

import java.time.Duration;

/**
 * @Title: Adapter to convert {@link PrometheusProperties} to a {@link PrometheusConfig}.
 * @Description:
 * @Company: seeyon.com
 * @Author ouyp
 * @Date: 2020/7/9 19:58
 */
public class PrometheusPropertiesConfigAdapter extends PropertiesConfigAdapter<PrometheusProperties> implements PrometheusConfig {
    PrometheusPropertiesConfigAdapter(PrometheusProperties properties) {
        super(properties);
    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public boolean descriptions() {
        return get(PrometheusProperties::isDescriptions,
                PrometheusConfig.super::descriptions);
    }

    @Override
    public Duration step() {
        return get(PrometheusProperties::getStep, PrometheusConfig.super::step);
    }

}
