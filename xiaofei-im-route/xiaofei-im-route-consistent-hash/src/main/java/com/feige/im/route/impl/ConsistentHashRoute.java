package com.feige.im.route.impl;

import com.feige.im.route.IRoute;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author feige<br />
 * @ClassName: ConsistentHashRoute <br/>
 * @Description: 带虚拟节点的一致性哈希路由<br/>
 * @date: 2021/10/27 22:44<br/>
 */
public class ConsistentHashRoute implements IRoute {


    public static final int VIRTUAL_NODE_COUNT = 5;


    private TreeMap<Integer, String> map;
    private volatile boolean flag = true;
    private final Object lock = new Object();

    public static void main(String[] args) {
        String[] strings = {"192.168.0.106:8001", "192.168.0.106:8002","192.168.0.106:8003"};
        ConsistentHashRoute consistentHashRoute = new ConsistentHashRoute();
        consistentHashRoute.add(strings);
        String route1 = consistentHashRoute.getRoute("mymy");
        String route2 = consistentHashRoute.getRoute("receiver");
        System.out.println(route1);
        System.out.println(route2);


    }



    @Override
    public void add(Collection<String> servers){
        synchronized (lock){
            try {
                flag = false;
                TreeMap<Integer, String> treeMap = new TreeMap<>();
                for (String server : servers) {
                    treeMap.put(hash(server),server);
                    for (int i = 0; i < VIRTUAL_NODE_COUNT; i++) {
                        treeMap.put(hash("vir:" + server + ":node" + i),server);
                    }
                }
                this.map = treeMap;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                flag = true;
            }
        }
    }

    @Override
    public void online(String userId, String address) {

    }

    public void add(String[] servers){
        add(Arrays.asList(servers));
    }


    @Override
    public String getRoute(String userId) {
        if (!flag){
            synchronized (lock){
                return compute(userId);
            }
        }
        return compute(userId);
    }

    /**
     * 计算用户所在的机器
     * @param userId
     * @return
     */
    private String compute(String userId){
        SortedMap<Integer, String> sortedMap = map.tailMap(hash(userId));
        if (!sortedMap.isEmpty()) {
            return sortedMap.get(sortedMap.firstKey());
        }
        return map.firstEntry().getValue();
    }

    @Override
    public void offline(String userId) {

    }


    /**
     * FNV1_32_HASH算法
     * @param str
     * @return
     */
    public int hash(String str) {
        final int p = 16777619;
        int hash = (int)2166136261L;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash ^ str.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // 如果算出来的值为负数则取其绝对值
        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }
}
