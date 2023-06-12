package com.feige.fim.netty;

import com.feige.fim.api.AbstractClient;
import com.feige.fim.api.ServerStatusListener;
import com.feige.fim.codec.Codec;
import com.feige.fim.lg.Logs;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author feige<br />
 * @ClassName: NettyClient <br/>
 * @Description: <br/>
 * @date: 2023/6/4 15:30<br/>
 */
public class NettyClient extends AbstractClient {
    protected EventLoopGroup group;
    protected Bootstrap bootstrap;
    protected Channel channel;
    private final NettyCodecAdapter codec;

    public NettyClient(Codec codec) {
        super(codec);
        this.codec = new NettyCodecAdapter(getCodec());
    }


    @Override
    public void initialize() {
        this.group = createEventLoopGroup();
        this.bootstrap = new Bootstrap();
    }

    @Override
    public Codec getCodec() {
        return null;
    }

    @Override
    protected void doConnect(ServerStatusListener stopListener) {
        try {
            initBootstrap();
            ChannelFuture channelFuture = this.bootstrap
                    .connect(remoteAddress)
                    .addListener(future -> {
                        
                    });
            this.channel = channelFuture.channel();
        }catch (Throwable throwable){
            Logs.getInstance().error("server start exception", throwable);
        }
    }

    @Override
    protected void doStop(ServerStatusListener stopListener) {
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
                        pipeline.addLast(new HeartbeatHandler());
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true);
    }
    
}
