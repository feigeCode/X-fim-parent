package com.feige.im.cluster.test;

import com.feige.im.handler.DefaultMsgListener;
import com.feige.im.handler.client.ClusterClientMsgListener;
import com.feige.im.handler.server.ClusterMsgForwardListener;
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
public class ClusterTest1 {
    public static void main(String[] args) {
        Parser.registerDefaultParsing();
        ImBusinessServiceImpl imBusinessService = new ImBusinessServiceImpl();
        ClusterMsgForwardListener defaultClusterMsgForwardProcessor = new ClusterMsgForwardListener(new DefaultMsgListener(),imBusinessService);
        ClusterClientMsgListener defaultClientMsgProcessor = new ClusterClientMsgListener(imBusinessService);
        ClusterTask clusterTask = new ClusterTask(defaultClientMsgProcessor);
        ImServer.start(new File("E:\\project\\im\\beifen\\xiaofei-im-parent\\conf\\xiaofei-im1.properties"),defaultClusterMsgForwardProcessor, clusterTask);
    }
}
