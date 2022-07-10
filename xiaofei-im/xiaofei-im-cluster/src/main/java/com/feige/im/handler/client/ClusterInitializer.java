package com.feige.im.handler.client;

import com.feige.im.channel.ClusterChannel;
import com.feige.im.config.ImConfig;
import com.feige.im.constant.ChannelAttr;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.pojo.proto.Cluster;
import com.feige.im.utils.AssertUtil;
import com.feige.im.utils.NameThreadFactory;
import com.feige.im.utils.ScheduledThreadPoolExecutorUtil;
import com.feige.im.utils.StringUtil;
import io.netty.channel.Channel;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author feige<br />
 * @ClassName: ClusterInitial <br/>
 * @Description: <br/>
 * @date: 2021/11/11 22:09<br/>
 */
public class ClusterInitializer {
    private static final Logger LOG = LoggerFactory.getLogger();
    private final ClusterChannel CLUSTER_CHANNEL = ClusterChannel.getInstance();
    private static final HashedWheelTimer TIMER = new HashedWheelTimer(new NameThreadFactory("cluster-waiting-ack-timer-"));
    private static final Map<String, WaitingAckTimer> WAITING_ACK_TIMER_CONTAINER = new ConcurrentHashMap<>();
    private static final ScheduledThreadPoolExecutorUtil EXECUTOR_UTIL = ScheduledThreadPoolExecutorUtil.getInstance();
    // 重发集群连接消息间隔时间
    public static final Duration DURATION = Duration.ofSeconds(5);
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
        LOG.debugInfo("开始向nodeKey = {}的主机发送集群连接消息{}",getNodeKey(getInetSocketAddress()),StringUtil.printMsg(clusterAuth));
        this.channel.writeAndFlush(clusterAuth);
        LOG.debugInfo("nodeKey = {}的主机集群连接消息发送完成", getNodeKey(getInetSocketAddress()));
        add(getWaitingAckTimer(getNodeKey(getInetSocketAddress()), clusterAuth));
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

    public static void add(WaitingAckTimer waitingAckTimer){
        WAITING_ACK_TIMER_CONTAINER.put(waitingAckTimer.getNodeKey(), waitingAckTimer);
    }

    public static void remove(String nodeKey){
        WaitingAckTimer waitingAckTimer = WAITING_ACK_TIMER_CONTAINER.remove(nodeKey);
        waitingAckTimer.cancel();

    }

    public WaitingAckTimer getWaitingAckTimer(String nodeKey, Cluster.Node msg){
        return new WaitingAckTimer(nodeKey, msg);
    }

    public class WaitingAckTimer {

        private String nodeKey;

        private Cluster.Node msg;

        private Timeout timeout;

        public WaitingAckTimer(String nodeKey, Cluster.Node msg) {
            this.nodeKey = nodeKey;
            this.msg = msg;
            createTimeout();
        }

        public void createTimeout(){
            this.timeout = TIMER.newTimeout(ignore -> EXECUTOR_UTIL.execute(() -> {
                // 超时未收到ack的消息
                LOG.debugInfo("超时未收到集群连接ACK，开始向nodeKey = {}的重发送集群连接消息{}", getNodeKey(), StringUtil.printMsg(msg));
                channel.writeAndFlush(this.msg);
                LOG.debugInfo("nodeKey = {}的主机集群连接消息重发送完成", getNodeKey());
                add(getWaitingAckTimer(ClusterInitializer.getNodeKey(getInetSocketAddress()), this.msg));
            }), DURATION.toMillis(), TimeUnit.MILLISECONDS);
        }


        public void cancel(){
            if (this.timeout == null || this.timeout.isCancelled() || this.timeout.isExpired()) {
                return;
            }
            this.timeout.cancel();
        }

        public String getNodeKey() {
            return nodeKey;
        }

        public void setNodeKey(String nodeKey) {
            this.nodeKey = nodeKey;
        }

        public Cluster.Node getMsg() {
            return msg;
        }

        public void setMsg(Cluster.Node msg) {
            this.msg = msg;
        }

        public Timeout getTimeout() {
            return timeout;
        }

        public void setTimeout(Timeout timeout) {
            this.timeout = timeout;
        }
    }
}
