package com.feige.fim;

import com.feige.api.sc.Server;
import com.feige.api.sc.ServerProvider;
import com.feige.framework.context.StandardApplicationContext;
import com.feige.framework.utils.Configs;

public class NettyServer {

    public static final String CONFIG_PATH = "E:\\project\\my\\X-fim-parent\\conf\\fim.yaml";
    
    public static void main(String[] args) throws Exception {
        createTcpServer();
    }
    public static void createTcpServer(){
        System.setProperty(Configs.CONFIG_FILE_KEY, CONFIG_PATH);
        StandardApplicationContext applicationContext = new StandardApplicationContext();
        final ServerProvider serverProvider = applicationContext.get("tcp", ServerProvider.class);
        Server server = serverProvider.get();
        boolean start = server.syncStart();
        System.out.println(start);
    }
}
