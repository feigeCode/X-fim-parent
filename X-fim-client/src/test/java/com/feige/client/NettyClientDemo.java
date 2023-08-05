package com.feige.client;

import com.feige.api.sc.Client;
import com.feige.fim.config.ClientConfig;
import com.feige.fim.utils.encrypt.AesUtils;
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
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClientKey(AesUtils.randomAesKey(16));
        clientConfig.setIv(AesUtils.randomAesIv(16));
        clientConfig.setClientId("1234");
        clientConfig.setClientVersion("1.0");
        clientConfig.setOsCode(1);
        clientConfig.setOsName("android");
        clientConfig.setOsVersion("14.0");
        clientConfig.setSessionId("12345");
        clientConfig.setToken("123");
        applicationContext.register("clientConfig", clientConfig);
        Client client = applicationContext.get("nettyClient", Client.class);
        client.syncStart();
    }
}
