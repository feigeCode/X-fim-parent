package com.feige.im.runnable;

import com.feige.discovery.pojo.ServerInstance;
import com.feige.im.client.ImClient;
import com.feige.im.handler.MsgProcessor;

import java.util.List;

/**
 * @author feige<br />
 * @ClassName: ClusterConnectTask <br/>
 * @Description: 和其他主机建立连接<br/>
 * @date: 2021/11/7 22:48<br/>
 */
public class ClusterConnectTask implements Runnable {

    private final List<ServerInstance> serverList;
    private final MsgProcessor processor;

    public ClusterConnectTask(List<ServerInstance> serverList, MsgProcessor processor) {
        this.serverList = serverList;
        this.processor = processor;
    }


    @Override
    public void run() {
        serverList.forEach(server -> ImClient.connect(server.getIp(),server.getPort(),processor));
    }
}
