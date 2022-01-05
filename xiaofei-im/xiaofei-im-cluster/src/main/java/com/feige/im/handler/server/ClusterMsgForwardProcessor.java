package com.feige.im.handler.server;

import com.feige.im.channel.ClusterChannel;
import com.feige.im.config.ImConfig;
import com.feige.im.constant.ChannelAttr;
import com.feige.im.constant.ProcessorEnum;
import com.feige.im.handler.MsgProcessor;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.DefaultMsg;
import com.feige.im.route.IRoute;
import com.feige.im.route.RouteManager;
import com.feige.im.utils.StringUtil;
import com.google.protobuf.Message;
import io.netty.channel.Channel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author feige<br />
 * @ClassName: ClusterMsgForwardProcessor <br/>
 * @Description: 用于处理集群消息的转发<br/>
 * @date: 2021/11/6 17:08<br/>
 */
public abstract class ClusterMsgForwardProcessor implements MsgProcessor {

    private static final Logger LOG = LoggerFactory.getLogger();
    private static final ClusterChannel clusterChannel = ClusterChannel.getINSTANCE();
    private static final ImConfig CONFIG = ImConfig.getInstance();
    private final MsgProcessor processor;
    private final IRoute route;

    public ClusterMsgForwardProcessor(MsgProcessor processor) {
        this.processor = processor;
        this.route = RouteManager.getIRoutes();

    }

    @Override
    public void process(ProcessorEnum key, Channel channel, Message msg, Throwable cause) {
        LOG.debugInfo("key={},channelId={},msg={}",key,channel.id().asShortText(),StringUtil.protoMsgFormat(msg));
        switch (key){
            case ACTIVE:
                LOG.debugInfo("server：连接进入channelId={}",channel.id().asShortText());
                break;
            case READ:
                msgHandler(channel,msg);
                break;
            case INACTIVE:
                clusterChannel.remove(channel);
                processor.process(key, channel, msg,null);
                break;
            case EXCEPTION:
                LOG.error("发生异常：",cause);
                break;
        }
    }


    /**
     * @description: 消息处理
     * @author: feige
     * @date: 2021/11/14 16:40
     * @param	channel
     * @param	msg
     * @return: void
     */
    public void msgHandler(Channel channel, Message msg){
        if (StringUtil.isEmpty(msg)){
            return;
        }
        // 集群连接消息请求
        if (msg instanceof DefaultMsg.ClusterAuth){
            clusterHandler(channel,msg);
            return;
        }

        if (msg instanceof DefaultMsg.Auth || msg instanceof DefaultMsg.Forced){
            processor.process(ProcessorEnum.READ,channel,msg,null);
            return;
        }

        // 消息转发
        msgForward(channel,msg);
    }


    /**
     * 集群连接消息处理
     * @param channel
     * @param msg
     */
    private void clusterHandler(Channel channel,Message msg){
        DefaultMsg.ClusterAuth clusterAuth = Parser.getT(DefaultMsg.ClusterAuth.class, msg);
        if (!StringUtil.isEmpty(clusterAuth) && !StringUtil.isEmpty(clusterAuth.getNodeKey())){
            String nodeKey = clusterAuth.getNodeKey();
            channel.attr(ChannelAttr.NODE_KEY).set(nodeKey);
            // 集群模式下管理管理各主机的Channel，方便通信
            clusterChannel.add(channel);
        }
    }

    /**
     * @description: 消息转发，如果接受者ID为空则不处理
     * @author: feige
     * @date: 2021/11/14 16:49
     * @param	channel	用户通道
     * @param	msg	接收到的消息
     * @return: void
     */
    private void msgForward(Channel channel, Message msg){
        Set<String> nodeKesSet = new HashSet<>();
        List<String> receiverIds = this.getReceiverIds(msg);
        if (receiverIds == null || receiverIds.isEmpty()){
            return;
        }
        for (String receiverId : receiverIds) {
            // 获取该用户所在的机器
            String nodeKey = this.route.getRoute(receiverId);
            // 本机的nodeKey
            String myNodeKey = CONFIG.getNodeKey();
            if (StringUtil.isEmpty(myNodeKey)){
                LOG.error("myNodeKey为空，请检查配置是否正确");
                return;
            }
            if (myNodeKey.equals(nodeKey)){
                // 如果在本机，则往下走
                processor.process(ProcessorEnum.READ, channel, msg,null);
            }else {
                // 存在多个用户在同一台机器，转发一次即可
                nodeKesSet.add(nodeKey);
            }
        }
        if (!nodeKesSet.isEmpty()){
            for (String nodeKey : nodeKesSet) {
                // 不在本机则转发到用户所在的机器
                clusterChannel.write(nodeKey,msg);
            }
        }
    }


     /**
      * @description: 获取消息接收者ID，通过接收者ID判断用户所在的主机，便于转发消息
      * @author: feige
      * @date: 2021/12/31 14:14
      * @param	message	用户自定义消息
      * @return: java.util.List<java.lang.String>
      */
    public abstract List<String> getReceiverIds(Message message);


}
