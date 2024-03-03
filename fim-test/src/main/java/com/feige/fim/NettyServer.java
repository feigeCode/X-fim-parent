package com.feige.fim;

import com.feige.api.sc.Server;
import com.feige.api.sc.ServerProvider;
import com.feige.framework.context.StandardApplicationContext;
import com.feige.framework.utils.Configs;
import com.feige.utils.javassist.ClassGenerator;

public class NettyServer {

    public static final String CONFIG_PATH = "E:\\project\\my\\X-fim-parent\\conf\\fim.yaml";
    
    public static void main(String[] args) throws Exception {
        createTcpServer(args);
    }
    public static void createTcpServer(String[] args){
        ClassGenerator.setDebugDump("E:\\project\\my\\X-fim-parent\\X-fim-test\\target\\classes");
        System.setProperty(Configs.CONFIG_FILE_KEY, CONFIG_PATH);
        StandardApplicationContext applicationContext = new StandardApplicationContext();
        applicationContext.getSpiCompLoader().addIgnoreImpl("com.feige.cache.redis.RedisCacheManagerFactory");
        applicationContext.start(args);
        final ServerProvider serverProvider = applicationContext.get("tcp", ServerProvider.class);
        Server server = serverProvider.get();
        boolean start = server.syncStart();
        System.out.println(start);
    }
}
