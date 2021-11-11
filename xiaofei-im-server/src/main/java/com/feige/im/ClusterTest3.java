package com.feige.im;

import com.feige.im.handler.ClusterMsgForwardProcessor;
import com.feige.im.handler.DefaultMsgProcessor;
import com.feige.im.pojo.Server;
import com.feige.im.route.impl.ConsistentHashRoute;
import com.feige.im.runnable.ClusterConnectTask;
import com.feige.im.server.ImServer;

import java.util.ArrayList;

/**
 * @author feige<br />
 * @ClassName: ClusterTest1 <br/>
 * @Description: <br/>
 * @date: 2021/11/7 23:13<br/>
 */
public class ClusterTest3 {
    public static void main(String[] args) {
        ArrayList<Server> servers = new ArrayList<>();
        servers.add(new Server("localhost",8090));
        servers.add(new Server("localhost",8100));
        ClusterMsgForwardProcessor processor = new ClusterMsgForwardProcessor(new DefaultMsgProcessor(), new ConsistentHashRoute());
        ClusterConnectTask clusterConnectTask = new ClusterConnectTask(servers, processor);
        ImServer.start(8120,processor,clusterConnectTask);
    }
}
