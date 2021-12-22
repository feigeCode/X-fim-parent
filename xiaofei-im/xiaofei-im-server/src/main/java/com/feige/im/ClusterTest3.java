package com.feige.im;

import com.feige.im.handler.client.DefaultClientMsgProcessor;
import com.feige.im.handler.server.ClusterMsgForwardProcessor;
import com.feige.im.handler.DefaultMsgProcessor;
import com.feige.im.handler.server.DefaultClusterMsgForwardProcessor;
import com.feige.im.server.ImServer;
import com.feige.im.task.ClusterTask;

import java.io.File;

/**
 * @author feige<br />
 * @ClassName: ClusterTest1 <br/>
 * @Description: <br/>
 * @date: 2021/11/7 23:13<br/>
 */
public class ClusterTest3 {
    public static void main(String[] args) {
        ClusterMsgForwardProcessor defaultClusterMsgForwardProcessor = new DefaultClusterMsgForwardProcessor(new DefaultMsgProcessor());
        DefaultClientMsgProcessor defaultClientMsgProcessor = new DefaultClientMsgProcessor();
        ClusterTask clusterTask = new ClusterTask(defaultClientMsgProcessor);
        ImServer.start(new File("E:\\project\\im\\xiaofei-im-parent\\conf\\xiaofei-im3.properties"),defaultClusterMsgForwardProcessor, clusterTask);
    }
}
