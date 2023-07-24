package com.feige.fim.server.tcp;


import com.feige.api.sc.Server;
import com.feige.fim.adapter.NettyCodecAdapter;
import com.feige.framework.config.Configs;
import com.feige.fim.factory.SslContextFactory;
import com.feige.fim.server.NettyServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author feige
 */
public class TcpServerInitializer extends ChannelInitializer<SocketChannel> {
    
    private final NettyCodecAdapter codec;
    private final NettyServerHandler serverHandler;
    private final SslContext sslContext;

    public TcpServerInitializer(Server server) {
        this.serverHandler = new NettyServerHandler(server.getSessionHandler(), server.getSessionRepository());
        this.codec = new NettyCodecAdapter(server.getCodec(), server.getSessionRepository());
        this.sslContext = buildSslContext();
        
    }

    private SslContext buildSslContext() {
        Boolean isEnableSsl = Configs.getBoolean(Configs.ConfigKey.SERVER_ENABLE_TCP_SSL, false);
        if (isEnableSsl) {
            return SslContextFactory.createSslContext(Configs.ConfigKey.SERVER_ENABLE_TCP_K_C_P,
                    Configs.ConfigKey.SERVER_ENABLE_TCP_P_K_P,
                    Configs.ConfigKey.SERVER_ENABLE_TCP_T_C_P,
                    Configs.ConfigKey.SERVER_ENABLE_TCP_K_P);
        }
        return null;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        if (sslContext != null){
            pipeline.addLast(sslContext.newHandler(socketChannel.alloc()));
        }
        pipeline.addLast(codec.getDecoder());
        pipeline.addLast(codec.getEncoder());
        pipeline.addLast(new IdleStateHandler(30,60,0,TimeUnit.SECONDS));
        pipeline.addLast(this.serverHandler);

    }


}
