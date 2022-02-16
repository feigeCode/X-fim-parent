package com.feige.im.handler.client;

import com.feige.im.channel.ClusterChannel;
import com.feige.im.config.ImConfig;
import com.feige.im.constant.ChannelAttr;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.pojo.proto.Cluster;
import com.feige.im.utils.AssertUtil;
import com.feige.im.utils.StringUtil;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;

/**
 * @author feige<br />
 * @ClassName: ClusterInitial <br/>
 * @Description: <br/>
 * @date: 2021/11/11 22:09<br/>
 */
public class ClusterInitializer {
    private static final Logger LOG = LoggerFactory.getLogger();
    private final ClusterChannel CLUSTER_CHANNEL = ClusterChannel.getInstance();
    private final ImConfig CONFIG = ImConfig.getInstance();
    private InetSocketAddress socketAddress;
    private Channel channel;
    /**
     * 初始化操作
     */
    public void init() {
        this.socketAddress = (InetSocketAddress) this.channel.remoteAddress();
        String nodeKey = getNodeKey(socketAddress);
        this.channel.attr(ChannelAttr.NODE_KEY).set(nodeKey);
        CLUSTER_CHANNEL.add(this.channel);
        sendClusterConnectMsg();
    }

    /**
     * 发送集群连接消息
     */
    public void sendClusterConnectMsg(){
        Cluster.Node clusterAuth = Cluster.Node.newBuilder()
                .setNodeKey(CONFIG.getNodeKey())
                .build();
        LOG.debugInfo("开始发送集群连接消息{}",StringUtil.protoMsgFormat(clusterAuth));
        this.channel.writeAndFlush(clusterAuth);
        LOG.debugInfo("集群连接消息发送完成");
    }

    /**
     * 获取节点key，用作Map的key，ip:port方便各节点之间通信
    * @param socketAddress
     * @return
     */
    public static String getNodeKey(InetSocketAddress socketAddress){
        AssertUtil.notNull(socketAddress,"socketAddress");
        return socketAddress.getAddress().getHostAddress() + ":" + socketAddress.getPort();
    }


    public InetSocketAddress getInetSocketAddress() {
        return socketAddress;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
