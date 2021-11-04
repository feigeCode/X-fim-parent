package com.feige.im.server;

import com.feige.im.handler.DefaultProtocolProcessor;
import com.feige.im.handler.ProtocolProcessor;
import com.feige.im.parser.Parser;
import com.feige.im.utils.NameThreadFactory;
import com.feige.im.utils.OsUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 * @author feige<br />
 * @ClassName: ImServer <br/>
 * @Description: 创建IM server<br/>
 * @date: 2021/10/6 20:16<br/>
 */
public class ImServer {

    private static final Logger log = LogManager.getLogger(ImServer.class);

    private final int port;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workGroup;
    private final Class<? extends ServerChannel> serverChannel;
    private final ProtocolProcessor processor;

    private ImServer(int port, ProtocolProcessor processor){
        this.port = port;
        this.processor = processor;
        if (OsUtil.isLinux()){
            this.bossGroup = new EpollEventLoopGroup(new NameThreadFactory("nio-boss-"));
            this.workGroup = new EpollEventLoopGroup(new NameThreadFactory("nio-work-"));
            this.serverChannel = EpollServerSocketChannel.class;
        }else {
            this.bossGroup = new NioEventLoopGroup(new NameThreadFactory("nio-boss-"));
            this.workGroup = new NioEventLoopGroup(new NameThreadFactory("nio-work-"));
            this.serverChannel = NioServerSocketChannel.class;
        }
    }

    public static void start(int port, ProtocolProcessor processor){
        new ImServer(port, processor).init();
    }

    /**
     * @description: 系统默认的端口和默认的消息处理器，默认的处理器功能受限，推荐使用自定义的
     * @author: feige
     * @date: 2021/10/10 13:26
     * @param
     * @return: void
     */
    public static void start(){
        start(8090, new DefaultProtocolProcessor());
    }

    private void init() {
        ChannelFuture channelFuture = new ServerBootstrap()
                .group(bossGroup, workGroup)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .localAddress(port)
                .channel(serverChannel)
                .childHandler(new NettyServerInitializer(processor))
                .bind().syncUninterruptibly();
        channelFuture.channel().newSucceededFuture().addListener(future -> {
            Parser.registerDefaultParsing();
            log.info("netty websocket server in {} port start finish....", this.port);
        });
        channelFuture.channel().closeFuture().addListener(future -> this.destroy());
    }

    public void destroy(){
        if (this.bossGroup != null){
            this.bossGroup.shutdownGracefully();
        }
        if (this.workGroup != null){
            this.workGroup.shutdownGracefully();
        }
    }


}
