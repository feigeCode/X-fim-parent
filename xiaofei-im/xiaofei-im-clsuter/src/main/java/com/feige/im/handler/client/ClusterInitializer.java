package com.feige.im.handler.client;

import com.feige.im.channel.ClusterChannel;
import com.feige.im.config.ImConfig;
import com.feige.im.constant.ChannelAttr;
import com.feige.im.pojo.proto.DefaultMsg;
import com.feige.im.utils.AssertUtil;
import com.feige.im.utils.StringUtil;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;

/**
 * @author feige<br />
 * @ClassName: ClusterInitial <br/>
 * @Description: <br/>
 * @date: 2021/11/11 22:09<br/>
 */
public class ClusterInitializer {
    private static final Logger LOG = LogManager.getLogger(ClusterInitializer.class);
    private static final ClusterChannel CLUSTER_CHANNEL = ClusterChannel.getINSTANCE();
    private static final ImConfig CONFIG = ImConfig.getInstance();

    private final Channel channel;
    public ClusterInitializer(Channel channel) {
        this.channel = channel;
    }

    /**
     * 初始化操作
     */
    public void init() {
        InetSocketAddress socketAddress = (InetSocketAddress) this.channel.remoteAddress();
        String nodeKey = getNodeKey(socketAddress);
        this.channel.attr(ChannelAttr.NODE_KEY).set(nodeKey);
        CLUSTER_CHANNEL.add(this.channel);
        sendClusterConnectMsg();
    }

    /**
     * 发送集群连接消息
     */
    public void sendClusterConnectMsg(){
        DefaultMsg.ClusterAuth clusterAuth = DefaultMsg.ClusterAuth.newBuilder()
                .setNodeKey(CONFIG.getNodeKey())
                .build();
        LOG.info("开始发送集群连接消息{}",() -> StringUtil.protoMsgFormat(clusterAuth));
        this.channel.writeAndFlush(clusterAuth);
        LOG.info("集群连接消息发送完成{}",() -> "");
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

}
