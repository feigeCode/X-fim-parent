package com.feige.im.server;


import com.feige.im.config.ImConfig;
import com.feige.im.constant.ImConst;
import com.feige.im.handler.DefaultMsgProcessor;
import com.feige.im.handler.MsgProcessor;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.DefaultMsg;
import com.feige.im.utils.AssertUtil;
import com.feige.im.utils.NameThreadFactory;
import com.feige.im.utils.OsUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
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

    private final int port;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workGroup;
    private final Class<? extends ServerChannel> serverChannel;
    private final MsgProcessor processor;
    private final Consumer<Integer> consumer;



    private ImServer(int port, MsgProcessor processor, Consumer<Integer> consumer){
        this.port = port;
        this.processor = processor;
        this.consumer = consumer;
        if (OsUtil.isLinux()){
            this.bossGroup = new EpollEventLoopGroup(new NameThreadFactory("server-nio-boss-"));
            this.workGroup = new EpollEventLoopGroup(new NameThreadFactory("server-nio-work-"));
            this.serverChannel = EpollServerSocketChannel.class;
        }else {
            this.bossGroup = new NioEventLoopGroup(new NameThreadFactory("server-nio-boss-"));
            this.workGroup = new NioEventLoopGroup(new NameThreadFactory("server-nio-work-"));
            this.serverChannel = NioServerSocketChannel.class;
        }
    }



    /**
     * @description: 通过配置文件启动，默认的消息处理器，默认的处理器功能受限，推荐使用自定义的
     * @author: feige
     * @date: 2021/11/14 1:02
     * @param	file 文件对象
     * @return: void
     */
    public static void start(File file){
        start(file,new DefaultMsgProcessor(),null);
    }

    /**
     * @description: 通过配置文件启动，默认的消息处理器，默认的处理器功能受限，推荐使用自定义的
     * @author: feige
     * @date: 2021/11/14 16:14
     * @param	is	配置文件流
     * @return: void
     */
    public static void start(InputStream is){
        start(is,new DefaultMsgProcessor(),null);
    }

    /**
     * @description:
     * @author: feige
     * @date: 2021/11/14 16:16
     * @param	file 配置文件对象
     * @param	processor	消息处理器
     * @param	consumer	集群任务
     * @return: void
     */
    public static void start(File file,MsgProcessor processor, Consumer<Integer> consumer){
        CONFIG.loadProperties(file);
        start0(processor,consumer);
    }

    /**
     * @description:
     * @author: feige
     * @date: 2021/11/14 16:16
     * @param	is 配置文件流
     * @param	processor	消息处理器
     * @param	consumer	集群任务
     * @return: void
     */
    public static void start(InputStream is, MsgProcessor processor, Consumer<Integer> consumer){
        CONFIG.loadProperties(is);
        start0(processor,consumer);
    }

    /**
     * @description: 运行启动器
     * @author: feige
     * @date: 2021/11/14 16:17
     * @param	processor	消息处理器
     * @param	consumer	集群任务
     * @return: void
     */
    public static void start0(MsgProcessor processor, Consumer<Integer> consumer){
        String port = CONFIG.getConfigByKey(ImConst.SERVER_PORT);
        AssertUtil.notBlank(port,"port");
        ImServer imServer = new ImServer(Integer.parseInt(port), processor,consumer);
        imServer.createServer();
    }

    /**
     * @description: 创建启动器
     * @author: feige
     * @date: 2021/11/14 16:19
     * @return: void
     */
    public void createServer() {
        ChannelFuture channelFuture = new ServerBootstrap()
                .group(bossGroup, workGroup)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .localAddress(port)
                .channel(serverChannel)
                .childHandler(new NettyServerInitializer(processor))
                .bind().syncUninterruptibly();
        channelFuture.channel().newSucceededFuture().addListener(future -> {
            if (future.isSuccess()) {
                LOG.info("netty websocket server in {} port start finish....", this.port);
            }else {
                LOG.error("netty websocket server in {} port start fail....", this.port);
            }
        });
        channelFuture.channel().closeFuture().addListener(future -> this.destroy());
        // 集群模式下需要和其他节点建立连接
        clusterConnect();
    }

   /**
    * @description: 关闭资源
    * @author: feige
    * @date: 2021/11/14 16:19
    * @return: void
    */
    public void destroy(){
        if (this.bossGroup != null){
            this.bossGroup.shutdownGracefully();
        }
        if (this.workGroup != null){
            this.workGroup.shutdownGracefully();
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
            Parser.add(5, DefaultMsg.ClusterAuth.class,DefaultMsg.ClusterAuth::parseFrom);
            consumer.accept(this.port);
        }
    }


}
