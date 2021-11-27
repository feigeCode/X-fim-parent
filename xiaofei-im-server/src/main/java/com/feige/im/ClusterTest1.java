package com.feige.im;

import com.feige.discovery.pojo.ServerInstance;
import com.feige.im.handler.client.DefaultClientMsgProcessor;
import com.feige.im.handler.server.ClusterMsgForwardProcessor;
import com.feige.im.handler.server.DefaultClusterMsgForwardProcessor;
import com.feige.im.handler.DefaultMsgProcessor;
import com.feige.im.task.ClusterTask;
import com.feige.im.server.ImServer;

import java.io.File;
import java.util.ArrayList;

/**
 * @author feige<br />
 * @ClassName: ClusterTest1 <br/>
 * @Description: <br/>
 * @date: 2021/11/7 23:13<br/>
 */
public class ClusterTest1 {
    public static void main(String[] args) {
        ClusterMsgForwardProcessor defaultClusterMsgForwardProcessor = new DefaultClusterMsgForwardProcessor(new DefaultMsgProcessor());
        DefaultClientMsgProcessor defaultClientMsgProcessor = new DefaultClientMsgProcessor();
        ClusterTask clusterTask = new ClusterTask(defaultClientMsgProcessor);
        ImServer.start(new File("E:\\project\\im\\xiaofei-im-parent\\conf\\xiaofei-im1.properties"),defaultClusterMsgForwardProcessor, clusterTask);
    }
}
