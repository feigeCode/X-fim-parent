package com.feige.api.constant;

public interface ServerConfigKey {


    /**
     * 运行模式（single单机、cluster集群）
     */
    String RUNNING_MODE = "fim.running.mode";

    String SERVER_ENABLE_TCP_KEY = "fim.server.tcp.enable";
    String SERVER_TCP_PROVIDER_NAME_KEY = "fim.server.tcp.provider.name";
    String SERVER_ENABLE_TCP_SSL = "fim.server.tcp.ssl.enable";
    String SERVER_ENABLE_TCP_K_C_P = "fim.server.tcp.ssl.key-cert-chain-path";
    String SERVER_ENABLE_TCP_P_K_P = "fim.server.tcp.ssl.private-key-path";
    String SERVER_ENABLE_TCP_K_P = "fim.server.tcp.ssl.key-password";
    String SERVER_ENABLE_TCP_T_C_P = "fim.server.tcp.ssl.trust-cert-collection-path";
    String SERVER_TCP_IP_KEY = "fim.server.tcp.ip";
    String SERVER_TCP_PORT_KEY = "fim.server.tcp.port";
    String SERVER_ENABLE_WS_KEY = "fim.server.ws.enable";
    String SERVER_WS_PROVIDER_NAME_KEY = "fim.server.ws.provider.name";
    String SERVER_ENABLE_WS_SSL = "fim.server.ws.ssl.enable";
    String SERVER_ENABLE_WS_K_C_P = "fim.server.ws.ssl.key-cert-chain-path";
    String SERVER_ENABLE_WS_P_K_P = "fim.server.ws.ssl.private-key-path";
    String SERVER_ENABLE_WS_K_P = "fim.server.ws.ssl.key-password";
    String SERVER_ENABLE_WS_T_C_P = "fim.server.ws.ssl.trust-cert-collection-path";
    String SERVER_ENABLE_HTTP_KEY = "fim.server.http.enable";
    String SERVER_WS_IP_KEY = "fim.server.ws.ip";
    String SERVER_WS_PORT_KEY = "fim.server.ws.port";
    String SERVER_ENABLE_UDP_KEY = "fim.server.udp.enable";
    String SERVER_UDP_IP_KEY = "fim.server.udp.ip";
    String SERVER_UDP_PORT_KEY = "fim.server.udp.port";


    /**
     * crypto
     */
    String SERVER_CRYPTO_ASYMMETRIC_PRI_K = "fim.crypto.asymmetric.private-key";

    /**
     * session
     */
    String SERVER_SESSION_EXPIRE_TIME = "fim.server.session.expire-time";
    /**
     * 注册中心
     */
    String NACOS_SERVER_LIST_KEY = "nacos.server-list";

    String REDIS_TYPE = "redis.type";
    String REDIS_CONFIG = "redis.config";


    /**
     * grpc client config
     */
    String GRPC_CLIENT_ADDRESS = "grpc.client.address";
    String GRPC_CLIENT_SCHEME = "grpc.client.scheme";
    String GRPC_CLIENT_LOAD_BALANCING_POLICY = "grpc.client.load-balancing-policy";
    String GRPC_CLIENT_EXTRA_PREFIX = "grpc.client.extra.";


    /**
     * grpc server config
     */
    String GRPC_SERVER_ADDRESS = "grpc.server.address";
    String GRPC_SERVER_PORT = "grpc.server.port";
    String GRPC_SERVER_EXTRA_PREFIX = "grpc.server.extra.";

}
