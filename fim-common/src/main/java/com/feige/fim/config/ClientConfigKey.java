package com.feige.fim.config;

public interface ClientConfigKey {
    String SESSION_PERSISTENT_KEY = "s.p.k";
    
    String FILE_SESSION_STORAGE_PATH_KEY = "fim.client.ss.path";
    String SESSION_ID = "s_d";
    String EXPIRE_TIME = "e_t";
    String CLIENT_KEY = "c_k";
    String IV = "iv";
    String CLIENT_VERSION = "c_v";
    String CLIENT_ID = "c_i";
    String OS_NAME = "o_n";
    String OS_VERSION = "o_v";
    String CLIENT_TYPE = "c_t";
    String TOKEN = "t";
    String SERVER_IP = "s_i";
    String SERVER_PORT = "s_p";
    
    String SERIALIZER_TYPE = "s_t";
    
    String TAGS = "tags";


    String CLIENT_ENABLE_TCP_SSL = "fim.client.tcp.ssl.enable";
    String CLIENT_TCP_K_C_P = "fim.client.tcp.ssl.key-cert-chain-path";
    String CLIENT_TCP_P_K_P = "fim.client.tcp.ssl.private-key-path";
    String CLIENT_TCP_K_P = "fim.client.tcp.ssl.key-password";
    String CLIENT_TCP_T_C_P = "fim.client.tcp.ssl.trust-cert-collection-path";
  
    
}
