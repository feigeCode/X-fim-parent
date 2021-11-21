package com.feige.im.route.impl;

import com.feige.im.route.IRoute;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author feige<br />
 * @ClassName: ConsistentHashRoute <br/>
 * @Description: 带虚拟节点的一致性哈希路由<br/>
 * @date: 2021/10/27 22:44<br/>
 */
public class ConsistentHashRoute implements IRoute {


    public static final int VIRTUAL_NODE_COUNT = 5;

    public static final String[] strings = {"192.168.0.107:8001", "192.168.0.107:8002"};

    private final TreeMap<Integer, String> MAP = new TreeMap<>();

    public static void main(String[] args) {
        String[] strings = {"192.168.0.107:8001", "192.168.0.107:8002"};
        ConsistentHashRoute consistentHashRoute = new ConsistentHashRoute(strings);
        String route1 = consistentHashRoute.getRoute("1");
        String route2 = consistentHashRoute.getRoute("2");
        System.out.println(route1);
        System.out.println(route2);

    }

    public ConsistentHashRoute(){
        this(strings);
    }

    public ConsistentHashRoute(Collection<String> servers){
        add(servers);
    }

    public ConsistentHashRoute(String[] servers){
        add(servers);
    }




    public void add(Collection<String> servers){
        for (String server : servers) {
            MAP.put(hash(server),server);
            for (int i = 0; i < VIRTUAL_NODE_COUNT; i++) {
                MAP.put(hash("vir:" + server + ":node" + i),server);
            }
        }
    }

    public void add(String[] servers){
        add(Arrays.asList(servers));
    }


    @Override
    public String getRoute(List<String> servers, String key) {
        add(servers);
        return getRoute(key);
    }

    @Override
    public String getRoute(String key) {
        SortedMap<Integer, String> sortedMap = MAP.tailMap(hash(key));
        if (!sortedMap.isEmpty()) {
            return sortedMap.get(sortedMap.firstKey());
        }
        return MAP.firstEntry().getValue();
    }

    /**
     * FNV1_32_HASH算法
     * @param str
     * @return
     */
    @Override
    public int hash(String str) {
        final int p = 16777619;
        int hash = (int)2166136261L;
        for (int i = 0; i < str.length(); i++)
            hash = (hash ^ str.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // 如果算出来的值为负数则取其绝对值
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }
}
