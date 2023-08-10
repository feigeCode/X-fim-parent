package com.feige.client;

import com.feige.api.constant.ClientType;
import com.feige.api.sc.Client;
import com.feige.fim.config.ClientConfig;
import com.feige.fim.utils.crypto.AesUtils;
import com.feige.fim.utils.crypto.CryptoUtils;
import com.feige.framework.api.context.ApplicationContext;
import com.feige.framework.context.StandardApplicationContext;
import com.feige.framework.extension.JdkSpiLoader;
import com.feige.framework.utils.Configs;


public class NettyClientDemo {

    public static final String CONFIG_PATH = "E:\\project\\my\\X-fim-parent\\conf\\fim-client.yaml";

    
    public static void main(String[] args) throws Exception {
        createClient();
    }
    
    public static void createClient(){
        System.setProperty(Configs.CONFIG_FILE_KEY, CONFIG_PATH);
        ApplicationContext applicationContext = new StandardApplicationContext(JdkSpiLoader.TYPE);
        ClientConfig.setClientKey(CryptoUtils.randomAesKey(16));
        ClientConfig.setIv(CryptoUtils.randomAesIv(16));
        ClientConfig.setEnableCrypto(true);
        ClientConfig.setClientId("1234");
        ClientConfig.setClientVersion("1.0");
        ClientConfig.setClientType(ClientType.ANDROID.getCode());
        ClientConfig.setOsName("android");
        ClientConfig.setOsVersion("14.0");
        ClientConfig.setSessionId("12345");
        ClientConfig.setToken("123");
        ClientConfig.setServerIp("127.0.0.1");
        ClientConfig.setServerPort(8001);
        Client client = applicationContext.get("nettyClient", Client.class);
        client.syncStart();
    }
}
