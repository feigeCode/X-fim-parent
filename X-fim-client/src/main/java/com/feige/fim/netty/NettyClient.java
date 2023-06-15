package com.feige.fim.netty;

import com.feige.fim.api.AbstractClient;
import com.feige.fim.api.MsgListener;
import com.feige.fim.api.PushService;
import com.feige.fim.api.ServerStatusListener;
import com.feige.fim.codec.Codec;
import com.feige.fim.event.ClientEvent;
import com.feige.fim.lg.Logs;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author feige<br />
 * @ClassName: NettyClientDemo <br/>
 * @Description: <br/>
 * @date: 2023/6/4 15:30<br/>
 */
public class NettyClient extends AbstractClient {
    protected EventLoopGroup group;
    protected Bootstrap bootstrap;
    protected Channel channel;
    private final NettyCodecAdapter codec;
    protected PushService pushService;

    public NettyClient(Codec codec, MsgListener msgListener) {
        super(codec, msgListener);
        this.codec = new NettyCodecAdapter(getCodec());
    }


    @Override
    public void initialize() {
        this.group = createEventLoopGroup();
        this.bootstrap = new Bootstrap();
    }

    @Override
    public PushService getPushService() {
        if (this.channel == null || !this.channel.isActive()){
            throw new RuntimeException("channel is null or no active");
        }
        if (pushService == null){
            pushService = new NettyPushService(this.channel);
        }
        return pushService;
    }


    @Override
    protected void doConnect(ServerStatusListener listener) {
        try {
            initBootstrap();
            ChannelFuture channelFuture = this.bootstrap
                    .connect(remoteAddress)
                    .addListener(future -> {
                        if (future.isSuccess()) {
                            listener.handle(new ClientEvent(NettyClient.this, ServerStatusListener.START_SUCCESS));
                        }else {
                            listener.handle(new ClientEvent(NettyClient.this, ServerStatusListener.START_FAILURE, future.cause()));
                        }
                    });
            this.channel = channelFuture.channel();
        }catch (Throwable throwable){
            Logs.getInstance().error("server start exception", throwable);
        }
    }

    @Override
    protected void doStop(ServerStatusListener listener) {
        Logs.getInstance().info("try shutdown {}...", this.getClass().getSimpleName());
        try {
            if (channel != null) {
                // unbind.
                channel.close();
                channel = null;
            }
        } catch (Throwable e) {
            Logs.getInstance().warn(e.getMessage(), e);
        }
        try {
            if (this.bootstrap != null) {
                long timeout = timeoutMillis();
                long quietPeriod = Math.min(2000L, timeout);
                if (this.group != null){
                    Future<?> bossGroupShutdownFuture = group.shutdownGracefully(quietPeriod, timeout, MILLISECONDS);
                    bossGroupShutdownFuture.syncUninterruptibly();
                }
                this.group = null;
                this.bootstrap = null;
            }
        } catch (Throwable e) {
            Logs.getInstance().warn(e.getMessage(), e);
        }
        listener.handle(new ClientEvent(NettyClient.this, ServerStatusListener.STOP_SUCCESS));
        Logs.getInstance().info("{} shutdown success.", this.getClass().getSimpleName());
    }


    
    protected int getGroupThreadNum(){
        return 2;
    }

    protected EventLoopGroup createEventLoopGroup() {
        return new NioEventLoopGroup(getGroupThreadNum());
    }

    protected Class<? extends SocketChannel> createSocketChannelClass() {
        return NioSocketChannel.class;
    }

    protected void initBootstrap() {
        this.bootstrap
                .group(group)
                .channel(createSocketChannelClass())
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(codec.getDecoder());
                        pipeline.addLast(codec.getEncoder());
                        pipeline.addLast(new IdleStateHandler(45,60,0, TimeUnit.SECONDS));
                        pipeline.addLast(new HeartbeatHandler(NettyClient.this));
                        pipeline.addLast(new SimpleChannelInboundHandler<Object>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
                                getMsgListener().onReceivedMsg(msg);
                            }
                        });
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true);
    }

    public Channel getChannel() {
        return channel;
    }

    public EventLoopGroup getGroup() {
        return group;
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }
    
}
