package com.feige.im.handler.server;

import com.feige.im.channel.ClusterChannel;
import com.feige.im.config.ClusterConfig;
import com.feige.im.constant.ChannelAttr;
import com.feige.im.constant.ImConst;
import com.feige.im.constant.ProcessorEnum;
import com.feige.im.handler.client.ClusterInitializer;
import com.feige.im.handler.MsgProcessor;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.DefaultMsg;
import com.feige.im.route.IRoute;
import com.feige.im.route.RouteManager;
import com.feige.im.utils.AssertUtil;
import com.feige.im.utils.StringUtil;
import com.google.protobuf.Message;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 * @author feige<br />
 * @ClassName: ClusterMsgForwardProcessor <br/>
 * @Description: 用于处理集群消息的转发<br/>
 * @date: 2021/11/6 17:08<br/>
 */
public abstract class ClusterMsgForwardProcessor implements MsgProcessor {

    private static final Logger LOG = LogManager.getLogger(ClusterMsgForwardProcessor.class);
    private static final ClusterChannel clusterChannel = ClusterChannel.getINSTANCE();
    private static final ClusterConfig CONFIG = ClusterConfig.getClusterConfig();
    private final MsgProcessor processor;
    private final IRoute route;

    public ClusterMsgForwardProcessor(MsgProcessor processor) {
        this.processor = processor;
        this.route = RouteManager.getIRoutes();
        AssertUtil.notNull(this.route,"route");
    }

    @Override
    public void process(ProcessorEnum key, Channel channel, Message msg, Throwable cause) {
        LOG.info("key={},channelId={},msg={}",() -> key,() -> channel.id().asShortText(),() -> StringUtil.protoMsgFormat(msg));
        switch (key){
            case ACTIVE:
                LOG.info("server：连接进入{}",() -> "");
                break;
            case READ:
                msgHandler(channel,msg);
                break;
            case INACTIVE:
                clusterChannel.remove(channel);
                processor.process(key, channel, msg,null);
                break;
            case EXCEPTION:
                System.out.println("发生异常");
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
        String receiverId = this.getReceiverId(msg);
        if (StringUtil.isBlank(receiverId)){
            return;
        }
        // 通过一致性hash算法判断该用户所在的机器
        String nodeKey = this.route.getRoute(receiverId);
        // 本机的nodeKey
        String myNodeKey = CONFIG.getNodeKey();
        if (StringUtil.isEmpty(myNodeKey)){
            LOG.error("{}为空，请检查配置是否正确",myNodeKey);
            return;
        }
        if (myNodeKey.equals(nodeKey)){
            // 如果在本机，则往下走
            processor.process(ProcessorEnum.READ, channel, msg,null);
        }else {
            // 不在本机则转发到用户所在的机器
            clusterChannel.write(nodeKey,msg);
        }
    }

    /**
     * @description: 获取消息接收者ID，通过接收者ID判断用户所在的主机，便于转发消息
     * @author: feige
     * @date: 2021/11/14 16:47
     * @param	message	用户自定义消息
     * @return: java.lang.String
     */
    public abstract String getReceiverId(Message message);


}
