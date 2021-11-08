package com.feige.im.handler;

import com.feige.im.channel.ClusterChannel;
import com.feige.im.config.ClusterConfig;
import com.feige.im.constant.ChannelAttr;
import com.feige.im.constant.ImConst;
import com.feige.im.constant.ProcessorEnum;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.DefaultMsg;
import com.feige.im.route.IRoute;
import com.feige.im.utils.StringUtil;
import com.google.protobuf.Message;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author feige<br />
 * @ClassName: ClusterProtocolProcessor <br/>
 * @Description: 用于处理集群消息的转发<br/>
 * @date: 2021/11/6 17:08<br/>
 */
public class ClusterProtocolProcessor implements ProtocolProcessor{

    private static final Logger LOG = LogManager.getLogger(ClusterProtocolProcessor.class);
    private static final ClusterChannel clusterChannel = ClusterChannel.getINSTANCE();
    private static final ClusterConfig config = ClusterConfig.getClusterConfig();
    private final ProtocolProcessor processor;
    private final IRoute route;

    public ClusterProtocolProcessor(ProtocolProcessor processor, IRoute route) {
        this.processor = processor;
        this.route = route;
    }

    @Override
    public void process(ProcessorEnum key, Channel channel, Message msg) {
        LOG.info("key={},channelId={},msg={}",() -> key,() -> channel.id().asShortText(),() -> StringUtil.protoMsgFormat(msg));
        switch (key){
            case READ:
                if (StringUtil.isEmpty(msg)){
                    return;
                }
                // 客户端发送心跳
                if (msg instanceof DefaultMsg.Ping){
                    System.out.println(msg);
                    channel.writeAndFlush(ImConst.PONG_MSG);
                }
                // 集群模式下管理管理各主机的Channel，方便通信
                if (msg instanceof DefaultMsg.ClusterAuth){
                    DefaultMsg.ClusterAuth clusterAuth = Parser.getT(DefaultMsg.ClusterAuth.class, msg);
                    if (!StringUtil.isEmpty(clusterAuth) && !StringUtil.isEmpty(clusterAuth.getNodeKey())){
                        String nodeKey = clusterAuth.getNodeKey();
                        channel.attr(ChannelAttr.NODE_KEY).set(nodeKey);
                        clusterChannel.add(channel);
                    }
                }else {
                    if (msg instanceof DefaultMsg.Msg){
                        DefaultMsg.Msg message = Parser.getT(DefaultMsg.Msg.class, msg);
                        if (!StringUtil.isEmpty(message) && !StringUtil.isEmpty(message.getReceiverId())){
                            String receiverId = message.getReceiverId();
                            // 通过一致性hash算法判断该用户所在的机器
                            String nodeKey = this.route.getRoute(receiverId);
                            String myNodeKey = config.getConfigByKey(ImConst.NODE_KEY);
                            if (StringUtil.isEmpty(myNodeKey)){
                                LOG.error("{}为空，请检查配置是否正确",ImConst.NODE_KEY);
                                return;
                            }
                            if (myNodeKey.equals(nodeKey)){
                                // 如果在本机，则往下走
                                processor.process(key, channel, msg);
                            }else {
                                // 不在本机则转发到用户所在的机器
                                clusterChannel.write(nodeKey,msg);
                            }
                        }
                    }
                }
                break;
            case INACTIVE:
                clusterChannel.remove(channel);
                processor.process(key, channel, msg);
                break;
        }
    }




}
