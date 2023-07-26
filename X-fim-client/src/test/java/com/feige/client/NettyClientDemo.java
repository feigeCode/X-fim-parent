package com.feige.client;

import com.feige.api.sc.Client;
import com.feige.framework.config.Configs;
import com.feige.framework.context.AppContext;


public class NettyClientDemo {

    public static final String CONFIG_PATH = "E:\\project\\my\\X-fim-parent\\conf\\fim-client.yaml";

    public static void initialize() throws Exception {
        System.out.println("initialize start...");
        System.setProperty(Configs.CONFIG_FILE_KEY, CONFIG_PATH);
        Configs.loadConfig();
        System.out.println("initialize end...");
    }
    
    
    public static void main(String[] args) throws Exception {
        initialize();
        createClient();
    }
    
    public static void createClient(){
        Client client = AppContext.get("nettyClient", Client.class);
        client.syncStart();
    }
}
