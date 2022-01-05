package com.feige.im.handler.client;

import com.feige.im.channel.ClusterChannel;
import com.feige.im.constant.ImConst;
import com.feige.im.constant.ProcessorEnum;
import com.feige.im.group.MyChannelGroup;
import com.feige.im.handler.MsgProcessor;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.pojo.proto.DefaultMsg;
import com.feige.im.utils.StringUtil;
import com.google.protobuf.Message;
import io.netty.channel.Channel;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author feige<br />
 * @ClassName: ClusterClientMsgProcessor <br/>
 * @Description: <br/>
 * @date: 2021/11/14 17:23<br/>
 */
public abstract class ClusterClientMsgProcessor implements MsgProcessor {

    private static final Logger LOG = LoggerFactory.getLogger();
    // 管理的是集群客户端的连接
    private static final ClusterChannel clusterChannel = ClusterChannel.getINSTANCE();
    // 管理的是用户客户端的连接
    private static final MyChannelGroup CHANNEL_GROUP = MyChannelGroup.getInstance();



    @Override
    public void process(ProcessorEnum key, Channel channel, Message msg, Throwable cause) {
        LOG.debugInfo("key={},channelId={},msg={}",key, channel.id().asShortText(), StringUtil.protoMsgFormat(msg));
        switch (key){
            case ACTIVE:
                Executors.newScheduledThreadPool(2).schedule(() -> {
                    init(channel);
                },5, TimeUnit.SECONDS);
                break;
            case READ:
                msgHandler(channel,msg);
                break;
            case INACTIVE:
                clusterChannel.remove(channel);
                break;
            case EXCEPTION:
                LOG.error("发生异常：",cause);
                break;
        }
    }

    public void init(Channel channel){
        LOG.debugInfo("client：连接进入");
        ClusterInitializer initializer = new ClusterInitializer(channel);
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
        if (msg instanceof DefaultMsg.Ping){
            LOG.debugInfo("客户端发送心跳");
            channel.writeAndFlush(ImConst.PONG_MSG);
            return;
        }
        List<String> receiverIds = this.getReceiverIds(msg);
        if (receiverIds == null || receiverIds.isEmpty()){
            return;
        }
        for (String receiverId : receiverIds) {
            CHANNEL_GROUP.write(receiverId,msg);
        }
    }


    /**
     * @description: 获取消息接收者ID，通过接收者ID推送消息
     * @author: feige
     * @date: 2021/11/14 16:47
     * @param	message	用户自定义消息
     * @return: java.lang.String
     */
    public abstract List<String> getReceiverIds(Message message);

}
