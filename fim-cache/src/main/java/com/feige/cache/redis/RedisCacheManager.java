package com.feige.cache.redis;


import com.feige.fim.cache.AbstractCacheManager;
import com.feige.fim.cache.Bucket;
import com.feige.fim.cache.CacheManager;
import com.feige.fim.cache.MapCache;
import com.feige.fim.constant.ServerConfigKey;
import com.feige.framework.aware.EnvironmentAware;
import com.feige.framework.env.api.Environment;
import com.feige.utils.spi.annotation.SPI;
import org.apache.commons.beanutils.BeanUtils;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;


@SPI(value = "redis", interfaces = CacheManager.class)
public class RedisCacheManager extends AbstractCacheManager implements EnvironmentAware {


    public static final String TYPE_SINGLE = "single";
    public static final String TYPE_SENTINEL = "sentinel";
    public static final String TYPE_CLUSTER = "cluster";

    private Environment env;
    private final RedissonClient client;

    public RedisCacheManager() throws InvocationTargetException, IllegalAccessException {
        this.client = this.getRedissonClient();
    }


    public RedissonClient getRedissonClient() throws InvocationTargetException, IllegalAccessException {
        String type = env.getString(ServerConfigKey.REDIS_TYPE);
        Map<String, Object> map = env.getMapByKeyPrefix(ServerConfigKey.REDIS_CONFIG);
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
    public void setEnvironment(Environment environment) {
        env = environment;
    }


    @Override
    public <K extends Serializable, V extends Serializable> MapCache<K, V> createMapCache(String name, Class<K> k, Class<V> v) {
        RMap<K, V> rMap = client.getMap(name);
        RedisMapCache<K, V> cache = new RedisMapCache<>(name, rMap);
        addCacheable(name, cache);
        return cache;
    }

    @Override
    public Bucket<String> createBucket(String name) {
        RBucket<String> bucket = client.getBucket(name);
        return new RedisBucket<>(name, bucket);
    }
}
