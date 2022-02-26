package com.feige.im.handler.client;

import com.feige.im.channel.ClusterChannel;
import com.feige.im.client.ImClient;
import com.feige.im.constant.ImConst;
import com.feige.im.group.MyChannelGroup;
import com.feige.im.handler.MsgListener;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.HeartBeat;
import com.feige.im.service.ImBusinessService;
import com.feige.im.utils.ScheduledThreadPoolExecutorUtil;
import com.feige.im.utils.StringUtil;
import com.google.protobuf.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author feige<br />
 * @ClassName: ClusterClientMsgListener <br/>
 * @Description: <br/>
 * @date: 2022/1/21 14:04<br/>
 */
public class ClusterClientMsgListener implements MsgListener {

    private static final Logger LOG = LoggerFactory.getLogger();
    /**
     * 管理的是集群客户端的连接
     */
    private final ClusterChannel clusterChannel = ClusterChannel.getInstance();
    /**
     * 管理的是用户客户端的连接
     */
    private final MyChannelGroup CHANNEL_GROUP = MyChannelGroup.getInstance();

    private final ClusterInitializer initializer = new ClusterInitializer();
    private final ScheduledThreadPoolExecutorUtil EXECUTOR_SERVICE =  ScheduledThreadPoolExecutorUtil.getInstance();

    private final AtomicInteger RETRY_COUNT = new AtomicInteger(0);

    private final ImBusinessService imBusinessService;

    public ClusterClientMsgListener(ImBusinessService imBusinessService) {
        this.imBusinessService = imBusinessService;
    }

    @Override
    public void active(ChannelHandlerContext ctx) {
        EXECUTOR_SERVICE.schedule(() -> {
            init(ctx.channel());
        },5, TimeUnit.SECONDS);
    }

    @Override
    public void read(ChannelHandlerContext ctx, Message msg) {
        msgHandler(ctx.channel(),msg);
    }

    @Override
    public void inactive(ChannelHandlerContext ctx) {
        clusterChannel.remove(ctx.channel());
        // 重连
        retry();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

    }

    public void init(Channel channel){
        LOG.debugInfo("client：连接进入");
        initializer.setChannel(channel);
        initializer.init();
    }

    /**
     * @description: 客户端接收来自服务端的消息，客户端把消息交给服务端，服务端再把消息推送到接收者
     * @author: feige
     * @date: 2021/11/14 18:04
     * @param	channel
     * @param	msg
     * @return: void
     */
    public void msgHandler(Channel channel, Message msg){
        if (StringUtil.isEmpty(msg)){
            return;
        }
        // 客户端发送心跳
        if (msg instanceof HeartBeat.Ping){
            LOG.debugInfo("客户端发送心跳");
            channel.writeAndFlush(ImConst.PONG_MSG);
            return;
        }
        // 获取接受者ID
        List<String> receiverIds = Parser.getReceiverIds(msg, imBusinessService);
        if (receiverIds == null || receiverIds.isEmpty()){
            return;
        }
        for (String receiverId : receiverIds) {
            CHANNEL_GROUP.write(receiverId,msg);
        }
    }

    public void retry(){
        if (RETRY_COUNT.get() < 3){
            EXECUTOR_SERVICE.schedule(() -> {
                InetSocketAddress inetSocketAddress = initializer.getInetSocketAddress();
                String hostAddress = inetSocketAddress.getAddress().getHostAddress();
                int port = inetSocketAddress.getPort();
                int incrementAndGet = RETRY_COUNT.incrementAndGet();
                LOG.info("重连开始，重试次数为：{},重连的主机ip={},port={}",incrementAndGet, hostAddress,port);
                ImClient.connect(hostAddress,port,this);
                retry();
            },5,TimeUnit.SECONDS);
        }

    }

}
