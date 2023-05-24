package com.feige.fim;

import com.feige.fim.config.Configs;
import com.feige.fim.lg.Loggers;
import com.feige.fim.spi.SpiLoaderUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ConfigTest {

    public static final String CONFIG_PATH = "E:\\project\\im\\X-fim-parent\\X-fim-common\\src\\test\\resources\\conf\\fim.yaml";
    String[] spiArr = {
            "com.feige.api.config.ConfigFactory"
    };

    @Test
    public void yamlConfigTest() throws Exception {
        System.setProperty(Configs.CONFIG_FILE_KEY, CONFIG_PATH);
        for (String spi : spiArr) {
            SpiLoaderUtils.load(spi);
        }
        Configs.loadConfig();
        Assert.assertEquals(Configs.getString("fim.test.key"), "value");
        Assert.assertTrue(CollectionUtils.isEqualCollection(Configs.getList("fim.test.arr"), Arrays.asList("a","b","c")));
        Assert.assertEquals(Configs.getString(Configs.CONFIG_FILE_KEY), CONFIG_PATH);
        Assert.assertEquals(Configs.getAppConfig().getString(Configs.CONFIG_FILE_KEY), "test");
    }
}
