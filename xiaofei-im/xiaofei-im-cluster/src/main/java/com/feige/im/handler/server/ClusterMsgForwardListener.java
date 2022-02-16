package com.feige.im.handler.server;

import com.feige.im.channel.ClusterChannel;
import com.feige.im.config.ImConfig;
import com.feige.im.constant.ChannelAttr;
import com.feige.im.handler.MsgListener;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.Cluster;
import com.feige.im.pojo.proto.DefaultMsg;
import com.feige.im.route.IRoute;
import com.feige.im.route.RouteManager;
import com.feige.im.utils.StringUtil;
import com.google.protobuf.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author feige<br />
 * @ClassName: ClusterMsgForwardListener <br/>
 * @Description: <br/>
 * @date: 2022/1/21 14:08<br/>
 */
public abstract class ClusterMsgForwardListener implements MsgListener {
    private static final Logger LOG = LoggerFactory.getLogger();
    private final ClusterChannel clusterChannel = ClusterChannel.getInstance();
    private final ImConfig CONFIG = ImConfig.getInstance();
    private final MsgListener msgListener;
    private final IRoute route;

    public ClusterMsgForwardListener(MsgListener msgListener) {
        this.msgListener = msgListener;
        this.route = RouteManager.getIRoutes();
    }

    @Override
    public void active(ChannelHandlerContext ctx) {
        LOG.debugInfo("server：连接进入channelId={}",ctx.channel().id().asShortText());
    }

    @Override
    public void read(ChannelHandlerContext ctx, Message msg) {
        msgHandler(ctx, msg);
    }

    @Override
    public void inactive(ChannelHandlerContext ctx) {
        clusterChannel.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOG.error("发生异常：",cause);
    }

    /**
     * @description: 消息处理
     * @author: feige
     * @date: 2021/11/14 16:40
     * @param	ctx
     * @param	msg
     * @return: void
     */
    public void msgHandler(ChannelHandlerContext ctx, Message msg){
        if (StringUtil.isEmpty(msg)){
            return;
        }
        // 集群连接消息请求
        if (msg instanceof Cluster.Node){
            clusterHandler(ctx.channel(), msg);
            return;
        }

        if (msg instanceof DefaultMsg.Auth || msg instanceof DefaultMsg.Forced){
            msgListener.read(ctx,msg);
            return;
        }

        // 消息转发
        msgForward(ctx,msg);
    }


    /**
     * 集群连接消息处理
     * @param channel
     * @param msg
     */
    private void clusterHandler(Channel channel,Message msg){
        Cluster.Node clusterAuth = Parser.getT(Cluster.Node.class, msg);
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
     * @param	ctx	用户通道
     * @param	msg	接收到的消息
     * @return: void
     */
    private void msgForward(ChannelHandlerContext ctx, Message msg){
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
                msgListener.read(ctx, msg);
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
