package com.feige.fim.app;

import com.feige.api.constant.ServerConfigKey;
import com.feige.api.sc.Server;
import com.feige.api.sc.ServerProvider;
import com.feige.framework.context.api.ApplicationContext;
import com.feige.framework.context.api.ApplicationRunner;
import com.feige.framework.env.api.Environment;
import com.feige.utils.spi.annotation.SpiComp;

@SpiComp(value = "tcpServer", interfaces = ApplicationRunner.class)
public class TcpApplicationRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationContext applicationContext, String... args) throws Exception {
        Environment environment = applicationContext.getEnvironment();
        Boolean tcpEnable = environment.getBoolean(ServerConfigKey.SERVER_ENABLE_TCP_KEY, Boolean.TRUE);
        String providerName = environment.getString(ServerConfigKey.SERVER_TCP_PROVIDER_NAME_KEY, "tcp");
        if (tcpEnable){
            ServerProvider serverProvider = applicationContext.get(providerName, ServerProvider.class);
            Server server = serverProvider.get();
            server.syncStart(); 
        }
    }
}
