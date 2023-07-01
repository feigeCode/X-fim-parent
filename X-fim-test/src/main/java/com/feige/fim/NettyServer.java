package com.feige.fim;

import com.feige.api.sc.Server;
import com.feige.api.sc.ServerProvider;
import com.feige.fim.config.Configs;
import com.feige.fim.spi.SpiLoaderUtils;

public class NettyServer {

    public static final String CONFIG_PATH = "E:\\project\\my\\X-fim-parent\\conf\\fim.yaml";
    
    public static void initialize() throws Exception {
        System.out.println("initialize start...");
        System.setProperty(Configs.CONFIG_FILE_KEY, CONFIG_PATH);
        Configs.loadConfig();
        System.out.println("initialize end...");
    }

    public static void main(String[] args) throws Exception {
        initialize();
        createTcpServer();
    }
    public static void createTcpServer(){
        final ServerProvider serverProvider = SpiLoaderUtils.get("tcp", ServerProvider.class);
        Server server = serverProvider.get();
        boolean start = server.syncStart();
        System.out.println(start);
    }
}