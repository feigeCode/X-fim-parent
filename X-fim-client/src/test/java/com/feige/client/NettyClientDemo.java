package com.feige.client;

import com.feige.api.sc.Client;
import com.feige.framework.context.StandardApplicationContext;
import com.feige.framework.utils.Configs;


public class NettyClientDemo {

    public static final String CONFIG_PATH = "E:\\project\\my\\X-fim-parent\\conf\\fim-client.yaml";

    
    public static void main(String[] args) throws Exception {
        createClient();
    }
    
    public static void createClient(){
        System.setProperty(Configs.CONFIG_FILE_KEY, CONFIG_PATH);
        StandardApplicationContext applicationContext = new StandardApplicationContext();
        Client client = applicationContext.get("nettyClient", Client.class);
        client.syncStart();
    }
}
