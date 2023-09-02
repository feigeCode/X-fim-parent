package com.feige.fim;

import com.feige.api.sc.Server;
import com.feige.api.sc.ServerProvider;
import com.feige.framework.module.StandardModuleContext;
import com.feige.framework.spi.JdkSpiCompLoader;
import com.feige.utils.javassist.ClassGenerator;
import com.feige.framework.context.StandardApplicationContext;
import com.feige.framework.utils.Configs;

public class NettyServer {

    public static final String CONFIG_PATH = "E:\\project\\my\\X-fim-parent\\conf\\fim.yaml";
    
    public static void main(String[] args) throws Exception {
        createTcpServer();
    }
    public static void createTcpServer(){
        ClassGenerator.setDebugDump("E:\\project\\my\\X-fim-parent\\X-fim-test\\target\\classes");
        System.setProperty(Configs.CONFIG_FILE_KEY, CONFIG_PATH);
        StandardApplicationContext applicationContext = new StandardApplicationContext(JdkSpiCompLoader.TYPE);
        applicationContext.getSpiCompLoader().addIgnoreImpl("com.feige.cache.redis.RedisCacheManagerFactory");
        applicationContext.initialize();
        final ServerProvider serverProvider = applicationContext.get("tcp", ServerProvider.class);
        Server server = serverProvider.get();
        boolean start = server.syncStart();
        System.out.println(start);
        StandardModuleContext testModule = new StandardModuleContext(applicationContext, "test");
        applicationContext.addModule(testModule);
    }
}
