package com.feige.utils.micrometer;

import com.feige.utils.common.StringUtils;
import com.sun.net.httpserver.HttpServer;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.jmx.JmxConfig;
import io.micrometer.jmx.JmxMeterRegistry;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.Duration;


@Slf4j
public class MeterRegistryHelper {
    private static final String JMX_DOMAIN_KEY = "management.metrics.export.jmx.domain";
    private static final String JMX_STEP_KEY = "management.metrics.export.jmx.step";
    private static final String PROMETHEUS_STEP_KEY = "management.metrics.export.prometheus.step";
    private static final String PROMETHEUS_SERVER_PORT_KEY = "management.metrics.export.prometheus.server.port";
    private static final String DEFAULT_JMX_DOMAIN = "ThreadPoolMetrics";
    private static final int DEFAULT_JMX_STEP = 1;

    private final CompositeMeterRegistry meterRegistry = Metrics.globalRegistry;
    private JmxMeterRegistry jmxMeterRegistry;
    private PrometheusMeterRegistry prometheusMeterRegistry;

    private MeterRegistryHelper() {
        // jmx
        JmxConfig jmxConfig = new JmxPropertiesConfigAdapter(jmxProperties());
        jmxMeterRegistry = new JmxMeterRegistry(jmxConfig, Clock.SYSTEM);
        Metrics.addRegistry(jmxMeterRegistry);

        // Prometheus
        String serverPort = System.getProperty(PROMETHEUS_SERVER_PORT_KEY);
        if (StringUtils.isNotBlank(serverPort)) {
            PrometheusPropertiesConfigAdapter config = new PrometheusPropertiesConfigAdapter(prometheusProperties());
            CollectorRegistry collectorRegistry = new CollectorRegistry(true);
            prometheusMeterRegistry = new PrometheusMeterRegistry(config, collectorRegistry, Clock.SYSTEM);
            Metrics.addRegistry(prometheusMeterRegistry);
            try {
                HttpServer server = HttpServer.create(new InetSocketAddress(Integer.parseInt(serverPort.trim())), 0);
                server.createContext("/prometheus", httpExchange -> {
                    String response = prometheusMeterRegistry.scrape();
                    httpExchange.sendResponseHeaders(200, response.getBytes().length);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                });
                new Thread(server::start).start();
                Runtime.getRuntime().addShutdownHook(new Thread(() -> server.stop(0)));
            } catch (Throwable e) {
                log.error("start Prometheus Server port=[" + serverPort + "] error: ", e);
            }
        }
    }

    private static JmxProperties jmxProperties() {
        JmxProperties jmxProperties = new JmxProperties();
        jmxProperties.setDomain(System.getProperty(JMX_DOMAIN_KEY, DEFAULT_JMX_DOMAIN));
        jmxProperties.setStep(Duration.ofMinutes(MapUtils.getIntValue(System.getProperties(), JMX_STEP_KEY, DEFAULT_JMX_STEP)));
        return jmxProperties;
    }

    private static PrometheusProperties prometheusProperties() {
        PrometheusProperties properties = new PrometheusProperties();
        properties.setStep(Duration.ofMinutes(MapUtils.getIntValue(System.getProperties(), PROMETHEUS_STEP_KEY, DEFAULT_JMX_STEP)));
         return properties;
    }

    /**
     * 获取计量器注册表
     *
     * @return
     */
    public static MeterRegistry getMeterRegistry() {
        return getInstance().meterRegistry;
    }

    public static MeterRegistryHelper getInstance(){
        return MeterRegistryHelperInner.INSTANCE;
    }
    private static class MeterRegistryHelperInner {
        private static final MeterRegistryHelper INSTANCE = new MeterRegistryHelper();
    }
}
