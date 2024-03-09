package com.feige.fim.client;

import com.feige.api.codec.Codec;
import com.feige.api.handler.SessionHandler;
import com.feige.api.sc.AbstractClient;
import com.feige.api.sc.Listener;
import com.feige.api.sc.ServiceException;
import com.feige.api.session.Session;
import com.feige.api.session.SessionRepository;
import com.feige.fim.adapter.NettyChannelHandlerAdapter;
import com.feige.fim.adapter.NettyCodecAdapter;
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
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author feige<br />
 * @ClassName: NettyClientDemo <br/>
 * @Description: <br/>
 * @date: 2023/6/4 15:30<br/>
 */
@Slf4j
@Getter
public class NettyClient extends AbstractClient {
    private EventLoopGroup group;
    private Bootstrap bootstrap;
    private Channel channel;
    private final NettyCodecAdapter codecAdapter;
    private final SslContext sslContext;


    public NettyClient(InetSocketAddress address, Codec codec, SessionHandler sessionHandler, SessionRepository sessionRepository, SslContext sslContext) {
        super(address, codec, sessionHandler, sessionRepository);
        this.codecAdapter = new NettyCodecAdapter(codec);
        this.sslContext = sslContext;
    }

    public NettyClient(InetSocketAddress address, Codec codec, SessionHandler sessionHandler, SessionRepository sessionRepository) {
        this(address, codec, sessionHandler, sessionRepository, null);
    }


    @Override
    public void initialize() {
        this.group = createEventLoopGroup();
        this.bootstrap = new Bootstrap();
    }
    
    @Override
    protected void doStart(Listener listener) {
        try {
            initBootstrap();
            ChannelFuture channelFuture = this.bootstrap
                    .connect(getAddress())
                    .addListener(future -> {
                        if (future.isSuccess()) {
                            connected.set(true);
                            reconnectCnt.set(0);
                           log.info("netty [{}] client in {} port connect finish....", getClass().getSimpleName() ,getAddress().getPort());
                            if (listener != null){
                                listener.onSuccess(getAddress());
                            }
                        }else {
                           log.error("server start failure on:" + getAddress().getPort(), future.cause());
                            if (listener != null) {
                                listener.onFailure(future.cause());
                            }
                        }
                    });
            this.channel = channelFuture.channel();
        }catch (Throwable throwable){
           log.error("server start exception", throwable);
        }
    }

    @Override
    protected void doStop(Listener listener) {
        if (!connected.compareAndSet(true, false)) {
            if (listener != null) {
                listener.onFailure(new ServiceException("server was already shutdown."));
            }
           log.warn("{} was already shutdown.", this.getClass().getSimpleName());
            return;
        }
       log.info("try shutdown {}...", this.getClass().getSimpleName());
        try {
            if (channel != null) {
                // unbind.
                channel.close();
                channel = null;
            }
        } catch (Throwable e) {
           log.warn(e.getMessage(), e);
        }
        try {
            if (this.bootstrap != null) {
                this.bootstrap = null;
            }
            long timeout = timeoutMillis();
            long quietPeriod = Math.min(2000L, timeout);
            if (this.group != null){
                Future<?> bossGroupShutdownFuture = group.shutdownGracefully(quietPeriod, timeout, MILLISECONDS);
                bossGroupShutdownFuture.syncUninterruptibly();
                this.group = null;
            }
        } catch (Throwable e) {
           log.warn(e.getMessage(), e);
        }
       log.info("{} shutdown success.", this.getClass().getSimpleName());
        if (listener != null) {
            listener.onSuccess(getAddress());
        }
        this.address = null;
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
    
    
    protected IdleStateHandler createIdleStateHandler(){
        return new IdleStateHandler(30,50,0, TimeUnit.SECONDS);
    }

    protected void initBootstrap() {
        this.bootstrap
                .group(group)
                .channel(createSocketChannelClass())
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        if (sslContext != null){
                            pipeline.addLast(sslContext.newHandler(channel.alloc()));
                        }
                        pipeline.addLast(codecAdapter.getTcpCodec());
                        pipeline.addLast(createIdleStateHandler());
                        pipeline.addLast(new NettyChannelHandlerAdapter(sessionHandler));
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true);
    }

    @Override
    public Session getSession() {
        return sessionRepository.get(channel.id().asShortText());
    }
}
