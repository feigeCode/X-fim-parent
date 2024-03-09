package com.feige.fim;

import com.feige.api.constant.ClientType;
import com.feige.api.sc.ClientProvider;
import com.feige.fim.config.ClientConfig;
import com.feige.framework.context.StandardApplicationContext;
import com.feige.framework.context.api.ApplicationContext;
import com.feige.framework.utils.Configs;
import com.feige.utils.crypto.CryptoUtils;


public class NettyClientDemo {

    public static final String CONFIG_PATH = "E:\\project\\my\\X-fim-parent\\conf\\fim-client.yaml";

    
    public static void main(String[] args) throws Exception {
        createClient(args);
    }
    
    public static void createClient(String[] args){
        System.setProperty(Configs.CONFIG_FILE_KEY, CONFIG_PATH);
        ApplicationContext applicationContext = new StandardApplicationContext();
        applicationContext.start(args);
        ClientConfig.setClientKey(CryptoUtils.randomAesKey(16));
        ClientConfig.setIv(CryptoUtils.randomAesIv(16));
        ClientConfig.setClientId("1234");
        ClientConfig.setClientVersion("1.0");
        ClientConfig.setClientType(ClientType.ANDROID.getCode());
        ClientConfig.setOsName("android");
        ClientConfig.setOsVersion("14.0");
        ClientConfig.setSessionId("12345");
        ClientConfig.setToken("123");
        ClientConfig.setServerIp("127.0.0.1");
        ClientConfig.setServerPort(8001);
        ClientConfig.setTags("test");
        ClientProvider clientProvider = applicationContext.get(ClientProvider.class);
        clientProvider.get().syncStart();
    }
}