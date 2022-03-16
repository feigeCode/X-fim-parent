package com.feige.im.server;


import com.feige.im.config.ImConfig;
import com.feige.im.constant.ImConst;
import com.feige.im.handler.DefaultMsgListener;
import com.feige.im.handler.MsgListener;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.Cluster;
import com.feige.im.utils.AssertUtil;
import com.feige.im.utils.NameThreadFactory;
import com.feige.im.utils.OsUtil;
import com.feige.im.utils.StringUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.File;
import java.io.InputStream;
import java.util.function.Consumer;


/**
 * @author feige<br />
 * @ClassName: ImServer <br/>
 * @Description: 创建IM server<br/>
 * @date: 2021/10/6 20:16<br/>
 */
public class ImServer {

    private static final Logger LOG = LoggerFactory.getLogger();
    private static final ImConfig CONFIG = ImConfig.getInstance();

    private int tcpPort;
    private EventLoopGroup tcpBossGroup;
    private EventLoopGroup tcpWorkGroup;
    private Class<? extends ServerChannel> tcpServerChannel;

    private int wsPort;
    private EventLoopGroup wsBossGroup;
    private EventLoopGroup wsWorkGroup;
    private Class<? extends ServerChannel> wsServerChannel;

    private int udpPort;
    private EventLoopGroup udpGroup;
    private Class<? extends DatagramChannel> udpChannel;

    private final MsgListener listener;
    private final Consumer<Integer> consumer;



    private ImServer(MsgListener listener, Consumer<Integer> consumer){
        this.listener = listener;
        this.consumer = consumer;
    }


    public void initTcp(){
        String port = CONFIG.getConfigByKey(ImConst.TCP_SERVER_PORT);
        AssertUtil.notBlank(port,"tcp port");
        this.tcpPort = Integer.parseInt(port);
        if (OsUtil.isLinux()){
            this.tcpBossGroup = new EpollEventLoopGroup(new NameThreadFactory("tcp-server-epoll-boss-"));
            this.tcpWorkGroup = new EpollEventLoopGroup(new NameThreadFactory("tcp-server-epoll-work-"));
            this.tcpServerChannel = EpollServerSocketChannel.class;
        }else {
            this.tcpBossGroup = new NioEventLoopGroup(new NameThreadFactory("tcp-server-nio-boss-"));
            this.tcpWorkGroup = new NioEventLoopGroup(new NameThreadFactory("tcp-server-nio-work-"));
            this.tcpServerChannel = NioServerSocketChannel.class;
        }
    }


    public void initWs(){
        String port = CONFIG.getConfigByKey(ImConst.WS_SERVER_PORT);
        AssertUtil.notBlank(port,"ws port");
        this.wsPort = Integer.parseInt(port);
        if (OsUtil.isLinux()){
            this.wsBossGroup = new EpollEventLoopGroup(new NameThreadFactory("ws-server-epoll-boss-"));
            this.wsWorkGroup = new EpollEventLoopGroup(new NameThreadFactory("ws-server-epoll-work-"));
            this.wsServerChannel = EpollServerSocketChannel.class;
        }else {
            this.wsBossGroup = new NioEventLoopGroup(new NameThreadFactory("ws-server-nio-boss-"));
            this.wsWorkGroup = new NioEventLoopGroup(new NameThreadFactory("ws-server-nio-work-"));
            this.wsServerChannel = NioServerSocketChannel.class;
        }
    }


    public void initUdp(){
        String port = CONFIG.getConfigByKey(ImConst.UDP_SERVER_PORT);
        AssertUtil.notBlank(port,"udp port");
        this.udpPort = Integer.parseInt(port);
        if (OsUtil.isLinux()){
            this.udpGroup = new EpollEventLoopGroup(new NameThreadFactory("udp-server-epoll-boss-"));
            this.udpChannel = EpollDatagramChannel.class;
        }else {
            this.udpGroup = new NioEventLoopGroup(new NameThreadFactory("udp-server-nio-boss-"));
            this.udpChannel = NioDatagramChannel.class;
        }
    }




    /**
     * @description: 通过配置文件启动，默认的消息监听器，默认的处理器功能受限，推荐使用自定义的
     * @author: feige
     * @date: 2021/11/14 1:02
     * @param	file 文件对象
     * @return: void
     */
    public static void start(File file){
        start(file,new DefaultMsgListener(),null);
    }

    /**
     * @description: 通过配置文件启动，默认的消息处理器，默认的处理器功能受限，推荐使用自定义的
     * @author: feige
     * @date: 2021/11/14 16:14
     * @param	is	配置文件流
     * @return: void
     */
    public static void start(InputStream is){
        start(is,new DefaultMsgListener(),null);
    }

    /**
     * @description:
     * @author: feige
     * @date: 2021/11/14 16:16
     * @param	file 配置文件对象
     * @param	listener	消息监听器
     * @param	consumer	集群任务
     * @return: void
     */
    public static void start(File file,MsgListener listener, Consumer<Integer> consumer){
        CONFIG.loadProperties(file);
        start0(listener,consumer);
    }

    /**
     * @description:
     * @author: feige
     * @date: 2021/11/14 16:16
     * @param	is 配置文件流
     * @param	listener	消息监听器
     * @param	consumer	集群任务
     * @return: void
     */
    public static void start(InputStream is, MsgListener listener, Consumer<Integer> consumer){
        CONFIG.loadProperties(is);
        start0(listener,consumer);
    }

    /**
     * @description: 运行启动器
     * @author: feige
     * @date: 2021/11/14 16:17
     * @param	listener	消息监听器
     * @param	consumer	集群任务
     * @return: void
     */
    public static void start0(MsgListener listener, Consumer<Integer> consumer){
        ImServer imServer = new ImServer(listener,consumer);
        String openProtocol = CONFIG.getConfigByKey(ImConst.OPEN_PROTOCOL);
        if (StringUtil.isBlank(openProtocol)){
            openProtocol = ImConst.ALL;
        }
        if (ImConst.ALL.equals(openProtocol) || openProtocol.toLowerCase().contains(ImConst.TCP)){
            imServer.initTcp();
            imServer.createTcpServer();
        }
        if (ImConst.ALL.equals(openProtocol) || openProtocol.toLowerCase().contains(ImConst.WS)){
            imServer.initWs();
            imServer.createWsServer();
        }
        if (ImConst.ALL.equals(openProtocol) || openProtocol.toLowerCase().contains(ImConst.UDP)){
            imServer.initUdp();
            imServer.createUdpServer();
        }

    }

    /**
     * @description: 创建Tcp启动器
     * @author: feige
     * @date: 2021/11/14 16:19
     * @return: void
     */
    public void createTcpServer() {
        ChannelFuture channelFuture = new ServerBootstrap()
                .group(tcpBossGroup, tcpWorkGroup)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .localAddress(tcpPort)
                .channel(tcpServerChannel)
                .childHandler(new TcpServerInitializer(listener))
                .bind().syncUninterruptibly();
        channelFuture.channel().newSucceededFuture().addListener(future -> {
            if (future.isSuccess()) {
                LOG.info("netty tcp server in {} port start finish....", this.tcpPort);
            }else {
                LOG.error("netty tcp server in {} port start fail....", this.tcpPort);
            }
        });
        channelFuture.channel().closeFuture().addListener(future -> this.destroy(tcpBossGroup,tcpWorkGroup));
        // 集群模式下需要和其他节点建立连接
        clusterConnect();
    }

    /**
     * @description: 创建Ws启动器
     * @author: feige
     * @date: 2021/11/14 16:19
     * @return: void
     */
    public void createWsServer() {
        String wsPath = CONFIG.getConfigByKey(ImConst.WS_PATH);
        if (StringUtil.isBlank(wsPath)){
            wsPath = ImConst.DEFAULT_WS_PATH;
        }
        ChannelFuture channelFuture = new ServerBootstrap()
                .group(wsBossGroup, wsWorkGroup)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .localAddress(wsPort)
                .channel(wsServerChannel)
                .childHandler(new WsServerInitializer(listener, wsPath))
                .bind().syncUninterruptibly();
        channelFuture.channel().newSucceededFuture().addListener(future -> {
            if (future.isSuccess()) {
                LOG.info("netty websocket server in {} port start finish....", this.wsPort);
            }else {
                LOG.error("netty websocket server in {} port start fail....", this.wsPort);
            }
        });
        channelFuture.channel().closeFuture().addListener(future -> this.destroy(wsBossGroup,wsWorkGroup));
    }

    /**
     * @description: 创建Udp启动器
     * @author: feige
     * @date: 2021/11/14 16:19
     * @return: void
     */
    public void createUdpServer(){
        ChannelFuture channelFuture = new Bootstrap()
                .group(udpGroup)
                .channel(udpChannel)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new UdpServerInitializer(listener))
                .localAddress(this.udpPort)
                .bind().syncUninterruptibly();
        channelFuture.channel().newSucceededFuture().addListener(future -> {
            if (future.isSuccess()) {
                LOG.info("netty udp server in {} port start finish....", this.udpPort);
            }else {
                LOG.error("netty udp server in {} port start fail....", this.udpPort);
            }
        });
        channelFuture.channel().closeFuture().addListener(future -> this.destroy(udpGroup,null));
    }

   /**
    * @description: 关闭资源
    * @param bossGroup
    * @param workGroup
    * @author: feige
    * @date: 2021/11/14 16:19
    * @return: void
    */
    public void destroy(EventLoopGroup bossGroup,EventLoopGroup workGroup){
        if (bossGroup != null && !(bossGroup.isShuttingDown() && bossGroup.isShutdown() && bossGroup.isTerminated())){
            bossGroup.shutdownGracefully();
        }
        if (workGroup != null && !(workGroup.isShuttingDown() && workGroup.isShutdown() && workGroup.isTerminated())){
            workGroup.shutdownGracefully();
        }
    }

    /**
     * @description: 集群模式下使用
     * @author: feige
     * @date: 2021/11/14 16:18
     * @return: void
     */
    public void clusterConnect(){
        if (consumer != null){
            LOG.info("集群任务开始");
            Parser.add(5, Cluster.Node.class,Cluster.Node::parseFrom);
            Parser.add(6, Cluster.InternalAck.class,Cluster.InternalAck::parseFrom);
            consumer.accept(this.tcpPort);
        }
    }


}
