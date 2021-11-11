package com.feige.im.client;

import com.feige.im.channel.ClusterChannel;
import com.feige.im.constant.ChannelAttr;
import com.feige.im.handler.MsgProcessor;
import com.feige.im.pojo.proto.DefaultMsg;
import com.feige.im.utils.NameThreadFactory;
import com.feige.im.utils.OsUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;



/**
 * @author feige<br />
 * @ClassName: ImClient <br/>
 * @Description: <br/>
 * @date: 2021/11/5 11:24<br/>
 */
public class ImClient {

    public static final Logger LOG = LogManager.getLogger(ImClient.class);
    public static final ClusterChannel clusterChannel = ClusterChannel.getINSTANCE();
    private final int port;
    private final String ip;
    private final EventLoopGroup wordGroup;
    private final Class<? extends SocketChannel> socketChannelClass;
    private final MsgProcessor processor;
    private Channel channel;

    public ImClient( String ip,int port, MsgProcessor processor) {
        this.ip = ip;
        this.port = port;
        this.processor = processor;
        if (OsUtil.isLinux()) {
            this.wordGroup = new EpollEventLoopGroup(new NameThreadFactory("client-nio-work-"));
            this.socketChannelClass = EpollSocketChannel.class;
        } else {
            this.wordGroup = new NioEventLoopGroup(new NameThreadFactory("client-nio-work-"));
            this.socketChannelClass = NioSocketChannel.class;
        }
    }

    public static void connect(String ip, int port, MsgProcessor processor) {
        ImClient imClient = new ImClient(ip, port, processor);
        imClient.createClient();

    }

    private void createClient() {
        ChannelFuture channelFuture = new Bootstrap()
                .channel(socketChannelClass)
                .group(this.wordGroup)
                .handler(new NettyClientInitializer(processor, ip, port))
                .connect(new InetSocketAddress(ip, port)).syncUninterruptibly();
        channelFuture.channel().closeFuture().addListener(future -> {
            this.destroy();
        });
        this.channel = channelFuture.channel();
        init();
        LOG.info("初始化工作完成");
    }

    /**
     * 初始化操作
     */
    public void init() {
        // 当客户端使用时需要注册消息类型
        // Parser.registerDefaultParsing();
        this.channel.attr(ChannelAttr.NODE_KEY).set(ip + ":" + port);
        clusterChannel.add(this.channel);
        LOG.info("与ip={},port={}的主机建立连接成功，开始初始化工作",() -> ip,() -> port);
        sendClusterConnectMsg();
    }

    public void sendClusterConnectMsg(){
        DefaultMsg.ClusterAuth clusterAuth = DefaultMsg.ClusterAuth.newBuilder()
                .setNodeKey("localhost:8100")
                .build();
        LOG.info("开始发送集群连接消息{}",() -> "");
        clusterChannel.write(ip + ":" + port, clusterAuth);
        LOG.info("集群连接消息发送完成{}",() -> "");
    }

    /**
     * 重连
     */
    public static void reconnect(){
        //
    }

    /**
     * 关闭
     */
    public void destroy(){
        if (this.wordGroup != null){
            this.wordGroup.shutdownGracefully();
        }
    }


}
