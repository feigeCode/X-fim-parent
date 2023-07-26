package com.feige.fim.server.ws;

import com.feige.api.sc.Server;
import com.feige.fim.adapter.NettyCodecAdapter;
import com.feige.fim.config.ServerConfigKey;
import com.feige.framework.config.Configs;
import com.feige.fim.factory.SslContextFactory;
import com.feige.fim.server.NettyServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;


public class WsServerInitializer extends ChannelInitializer<SocketChannel> {
    private final NettyCodecAdapter codec;
    private final NettyServerHandler serverHandler;
    private final String wsPath;
    private final SslContext sslContext;

    public WsServerInitializer(Server server, String wsPath) {
        this.serverHandler = new NettyServerHandler(server.getSessionHandler(), server.getSessionRepository());
        this.codec = new NettyCodecAdapter(server.getCodec(), server.getSessionRepository());
        this.wsPath = wsPath;
        this.sslContext = buildSslContext();
    }

    private SslContext buildSslContext() {
        Boolean isEnableSsl = Configs.getBoolean(ServerConfigKey.SERVER_ENABLE_WS_SSL, false);
        if (isEnableSsl){
            return SslContextFactory.createSslContext(ServerConfigKey.SERVER_ENABLE_WS_K_C_P,
                    ServerConfigKey.SERVER_ENABLE_WS_P_K_P,
                    ServerConfigKey.SERVER_ENABLE_WS_T_C_P,
                    ServerConfigKey.SERVER_ENABLE_WS_K_P);
        }
        return null;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        if (sslContext != null){
            pipeline.addLast(sslContext.newHandler(socketChannel.alloc()));
        }
        //基于http协议，所以要有http编码解码器
        pipeline.addLast(new HttpServerCodec());
        //对写大数据的支持
        pipeline.addLast(new ChunkedWriteHandler());
        //对httpMessage进行聚合，聚合成FullHttpRequest或FullHttpResponse
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));
        //websocket服务器处理的协议，用于指定给客户端连接访问的路由：/ws
        //处理握手动作：handshaking(close,ping,pong)ping + pong = 心跳
        //对于websocket来讲，都是以frames进行传输的，不同的数据类型对应的frames也不同
        pipeline.addLast(new WebSocketServerProtocolHandler(wsPath, null, true, 65536, false, true));
        pipeline.addLast(codec.getWsDecoder());
        pipeline.addLast(codec.getEncoder());
        pipeline.addLast(new IdleStateHandler(30,60,0, TimeUnit.SECONDS));
        pipeline.addLast(this.serverHandler);

    }
}
