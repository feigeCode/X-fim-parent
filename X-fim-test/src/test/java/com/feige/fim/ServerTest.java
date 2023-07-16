package com.feige.fim;

import com.feige.api.annotation.Inject;
import com.feige.api.annotation.Value;
import com.feige.api.sc.Server;
import com.feige.api.sc.ServerProvider;
import com.feige.api.spi.InstanceProvider;
import com.feige.fim.codec.PacketCodecInstanceProvider;
import com.feige.fim.config.Configs;
import com.feige.fim.server.NettyTcpServerProvider;
import com.feige.fim.context.AppContext;
import com.feige.fim.utils.ReflectionUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ServerTest {
    public static final String CONFIG_PATH = "E:\\project\\my\\X-fim-parent\\conf\\fim.yaml";
    
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
        final ServerProvider serverProvider = AppContext.get("tcp", ServerProvider.class);
        Server server = serverProvider.get();
        server.syncStart();
        server.syncStop();
    }

    @Test
    public void createWsServer(){
        final ServerProvider serverProvider = AppContext.get("ws", ServerProvider.class);
        Server server = serverProvider.get();
        server.syncStart();
        server.syncStop();
    }
    
    @Test
    public void reflectionTest(){
        List<Field> list = new ArrayList<>();
        // 遍历类的所有字段，包括父类的字段
        ReflectionUtils.doWithFields(NettyTcpServerProvider.class, field -> {
            if (field.isAnnotationPresent(Inject.class) || field.isAnnotationPresent(Value.class)) {
                list.add(field);
            }
        });
        Assert.assertEquals(list.size(), 5);
    }

    @Test
    public void implTest(){
        Assert.assertTrue(InstanceProvider.class.isAssignableFrom(PacketCodecInstanceProvider.class));
    }
}
