package com.feige.im.client;

import com.feige.im.handler.MsgProcessor;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
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

import java.net.InetSocketAddress;



/**
 * @author feige<br />
 * @ClassName: ImClient <br/>
 * @Description: <br/>
 * @date: 2021/11/5 11:24<br/>
 */
public class ImClient {

    public static final Logger LOG = LoggerFactory.getLogger();
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

    public static ImClient connect(String ip, int port, MsgProcessor processor) {
        ImClient imClient = new ImClient(ip, port, processor);
        imClient.createClient();
        return imClient;
    }

    private void createClient() {
        ChannelFuture channelFuture = new Bootstrap()
                .channel(socketChannelClass)
                .group(this.wordGroup)
                .handler(new NettyClientInitializer(processor))
                .connect(new InetSocketAddress(ip, port)).syncUninterruptibly();
        channelFuture.channel().closeFuture().addListener(future -> this.destroy());
        channelFuture.channel().newSucceededFuture().addListener(future -> {
            if (future.isSuccess()) {
                LOG.info("与ip={},port={}的主机建立连接成功！",ip,port);
            }else {
                LOG.error("与ip={},port={}的主机建立连接失败！",ip,port);
            }
        });
        this.channel = channelFuture.channel();

    }

    /**
     * 关闭
     */
    public void destroy(){
        if (this.wordGroup != null){
            this.wordGroup.shutdownGracefully();
        }
    }


    public Channel getChannel() {
        return channel;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public MsgProcessor getProcessor() {
        return processor;
    }
}
