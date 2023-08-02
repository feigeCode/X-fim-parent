package com.feige.client;

import com.feige.api.sc.Client;
import com.feige.fim.config.ClientConfig;
import com.feige.framework.api.context.ApplicationContext;
import com.feige.framework.context.StandardApplicationContext;
import com.feige.framework.extension.JdkSpiLoader;
import com.feige.framework.utils.Configs;

import java.util.Collections;


public class NettyClientDemo {

    public static final String CONFIG_PATH = "E:\\project\\my\\X-fim-parent\\conf\\fim-client.yaml";

    
    public static void main(String[] args) throws Exception {
        createClient();
    }
    
    public static void createClient(){
        System.setProperty(Configs.CONFIG_FILE_KEY, CONFIG_PATH);
        ApplicationContext applicationContext = new StandardApplicationContext(JdkSpiLoader.TYPE);
        ClientConfig clientConfig = new ClientConfig();
        applicationContext.register(ClientConfig.class, Collections.singletonList(clientConfig));
        Client client = applicationContext.get("nettyClient", Client.class);
        client.syncStart();
    }
}
