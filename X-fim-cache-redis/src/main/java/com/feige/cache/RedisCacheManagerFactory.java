package com.feige.cache;

import com.feige.api.annotation.SpiComp;
import com.feige.api.cache.CacheManager;
import com.feige.api.cache.CacheManagerFactory;
import com.feige.fim.config.Configs;
import org.apache.commons.beanutils.BeanUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@SpiComp("redis")
public class RedisCacheManagerFactory implements CacheManagerFactory {
    

    public static final String TYPE_SINGLE = "single";
    public static final String TYPE_SENTINEL = "sentinel";
    public static final String TYPE_CLUSTER = "cluster";

    public RedissonClient getRedissonClient() throws InvocationTargetException, IllegalAccessException {
        String type = Configs.getString(Configs.ConfigKey.REDIS_TYPE);
        Map<String, Object> map = Configs.getMap(Configs.ConfigKey.REDIS_CONFIG);
        Config config = new Config();
        switch (type) {
            case TYPE_SINGLE:
                setupSingle(config, map);
                break;
            case TYPE_SENTINEL:
                setupSentinel(config, map);
                break;
            case TYPE_CLUSTER:
                setupCluster(config, map);
                break;
        }
        return Redisson.create(config);
    }

    private void setupCluster(Config config, Map<String, Object> map) throws InvocationTargetException, IllegalAccessException {
        ClusterServersConfig cfg = config.useClusterServers();
        BeanUtils.copyProperties(cfg, map);
    }

    private void setupSentinel(Config config, Map<String, Object> map) throws InvocationTargetException, IllegalAccessException {
        SentinelServersConfig cfg = config.useSentinelServers();
        BeanUtils.copyProperties(cfg, map);
    }

    private void setupSingle(Config config, Map<String, Object> map) throws InvocationTargetException, IllegalAccessException {
        SingleServerConfig cfg = config.useSingleServer();
        BeanUtils.copyProperties(cfg, map);
    }

    
    @Override
    public CacheManager create() throws Exception {
        RedissonClient client = getRedissonClient();
        return new RedisCacheManager(client);
    }

}
