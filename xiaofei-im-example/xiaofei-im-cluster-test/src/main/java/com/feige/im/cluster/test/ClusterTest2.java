package com.feige.im.cluster.test;

import com.feige.im.handler.DefaultMsgListener;
import com.feige.im.handler.client.DefaultClusterClientMsgListener;
import com.feige.im.handler.server.DefaultClusterMsgForwardListener;

import com.feige.im.parser.Parser;
import com.feige.im.service.impl.ImBusinessServiceImpl;
import com.feige.im.task.ClusterTask;
import com.feige.im.server.ImServer;

import java.io.File;

/**
 * @author feige<br />
 * @ClassName: ClusterTest1 <br/>
 * @Description: <br/>
 * @date: 2021/11/7 23:13<br/>
 */
public class ClusterTest2 {
    public static void main(String[] args) {
        Parser.registerDefaultParsing();
        ImBusinessServiceImpl imBusinessService = new ImBusinessServiceImpl();
        DefaultClusterMsgForwardListener defaultClusterMsgForwardProcessor = new DefaultClusterMsgForwardListener(new DefaultMsgListener(),imBusinessService);
        DefaultClusterClientMsgListener defaultClientMsgProcessor = new DefaultClusterClientMsgListener(imBusinessService);
        ClusterTask clusterTask = new ClusterTask(defaultClientMsgProcessor);
        ImServer.start(new File("E:\\project\\im\\xiaofei-im-parent\\conf\\xiaofei-im2.properties"),defaultClusterMsgForwardProcessor, clusterTask);
    }
}
