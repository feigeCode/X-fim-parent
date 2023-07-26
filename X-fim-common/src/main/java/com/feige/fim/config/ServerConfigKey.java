package com.feige.fim.config;

public interface ServerConfigKey {
        /**
         * log config key
         */
        String LOG_DIR = "fim.log.dir";
        String LOG_LEVEL = "fim.log.level";
        String LOG_CONF_PATH = "fim.log.conf-path";
        /**
         * server config key
         */
        String SERVER_ENABLE_EPOLL_KEY = "fim.server.enable-epoll";
        String SERVER_ENABLE_TCP_KEY = "fim.server.tcp.enable";
        String SERVER_ENABLE_TCP_SSL = "fim.server.tcp.ssl.enable";
        String SERVER_ENABLE_TCP_K_C_P = "fim.server.tcp.ssl.key-cert-chain-path";
        String SERVER_ENABLE_TCP_P_K_P = "fim.server.tcp.ssl.private-key-path";
        String SERVER_ENABLE_TCP_K_P = "fim.server.tcp.ssl.key-password";
        String SERVER_ENABLE_TCP_T_C_P = "fim.server.tcp.ssl.trust-cert-collection-path";
        String SERVER_TCP_IP_KEY = "fim.server.tcp.ip";
        String SERVER_TCP_PORT_KEY = "fim.server.tcp.port";
        String SERVER_ENABLE_WS_KEY = "fim.server.ws.enable";
        String SERVER_ENABLE_WS_SSL = "fim.server.ws.ssl.enable";
        String SERVER_ENABLE_WS_K_C_P = "fim.server.tcp.ssl.key-cert-chain-path";
        String SERVER_ENABLE_WS_P_K_P = "fim.server.tcp.ssl.private-key-path";
        String SERVER_ENABLE_WS_K_P = "fim.server.tcp.ssl.key-password";
        String SERVER_ENABLE_WS_T_C_P = "fim.server.tcp.ssl.trust-cert-collection-path";
        String SERVER_ENABLE_HTTP_KEY = "fim.server.http.enable";
        String SERVER_WS_IP_KEY = "fim.server.ws.ip";
        String SERVER_WS_PORT_KEY = "fim.server.ws.port";
        String SERVER_ENABLE_UDP_KEY = "fim.server.udp.enable";
        String SERVER_UDP_IP_KEY = "fim.server.udp.ip";
        String SERVER_UDP_PORT_KEY = "fim.server.udp.port";


       

        /**
         * 注册中心
         */
        String NACOS_SERVER_LIST_KEY = "nacos.server-list";
        
        String REDIS_TYPE = "redis.type";
        String REDIS_CONFIG = "redis.config";
        
    }
