package com.feige.fim;

import com.feige.api.sc.Server;
import com.feige.api.sc.ServerProvider;
import com.feige.fim.config.Configs;
import com.feige.fim.spi.SpiLoaderUtils;
import org.junit.Before;
import org.junit.Test;

public class ServerTest {
    
    @Before
    public void initialize() throws Exception {
        Configs.loadConfig();
    }
    
    @Test
    public void createTcpServer(){
        final ServerProvider serverProvider = SpiLoaderUtils.get("tcp", ServerProvider.class);
        Server server = serverProvider.get();
        server.syncStart();
    }
}
