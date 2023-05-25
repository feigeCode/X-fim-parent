package com.feige.fim;

import com.feige.api.sc.Server;
import com.feige.api.sc.ServerProvider;
import com.feige.fim.config.Configs;
import com.feige.fim.spi.SpiLoaderUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ServerTest {
    public static final String CONFIG_PATH = "E:\\project\\im\\X-fim-parent\\conf\\fim.yaml";
    
    @BeforeClass
    public static void initialize() throws Exception {
        System.out.println("initialize start...");
        System.setProperty(Configs.CONFIG_FILE_KEY, CONFIG_PATH);
        Configs.loadConfig();
        System.out.println("initialize end...");
    }
    
    @Test
    public void configTest(){
        Assert.assertEquals(Configs.getString(Configs.CONFIG_FILE_KEY), CONFIG_PATH);
        Assert.assertEquals(Configs.getString(Configs.ConfigKey.LOG_LEVEL), "debug");
    }
    
    @Test
    public void createTcpServer(){
        final ServerProvider serverProvider = SpiLoaderUtils.get("tcp", ServerProvider.class);
        Server server = serverProvider.get();
        server.syncStart();
        server.syncStop();
    }

    @Test
    public void createWsServer(){
        final ServerProvider serverProvider = SpiLoaderUtils.get("ws", ServerProvider.class);
        Server server = serverProvider.get();
        server.syncStart();
        server.syncStop();
    }
}
