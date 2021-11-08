package com.feige.im.client;

import com.feige.im.codec.XiaoFeiProtoBufDecoder;
import com.feige.im.codec.XiaoFeiProtoBufEncoder;
import com.feige.im.handler.ProtocolProcessor;
import com.feige.im.handler.ServerHeartbeatHandler;
import com.feige.im.handler.XiaoFeiImHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * @author feige<br />
 * @ClassName: NettyClientInitializer <br/>
 * @Description: <br/>
 * @date: 2021/11/7 18:34<br/>
 */
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    private final ProtocolProcessor processor;
    private final String ip;
    private final int port;

    public NettyClientInitializer(ProtocolProcessor processor, String ip, int port) {
        this.processor = processor;
        this.ip = ip;
        this.port = port;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        URI uri = new URI("ws",null,ip,port,"/ws",null,null);
        WebSocketClientHandshaker handShaker = WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, false, new DefaultHttpHeaders());
        ChannelPipeline pipeline = ch.pipeline();
        //基于http协议，所以要有http编码解码器
        pipeline.addLast(new HttpClientCodec());
        // 自定义proto编解码器
        pipeline.addLast(new XiaoFeiProtoBufDecoder());
        pipeline.addLast(new XiaoFeiProtoBufEncoder());
        //对写大数据的支持
        pipeline.addLast(new ChunkedWriteHandler());
        //对httpMessage进行聚合，聚合成FullHttpRequest或FullHttpResponse
        pipeline.addLast(new HttpObjectAggregator(1024*64));
        //==============以上是用于支持http协议=============
        //websocket服务器处理的协议，用于指定给客户端连接访问的路由：/ws
        //本handler会帮你处理一些繁重的的复杂的事
        //会帮你处理握手动作：handshaking(close,ping,pong)ping + pong = 心跳
        //对于websocket来讲，都是以frames进行传输的，不同的数据类型对应的frames也不同
        pipeline.addLast(new WebSocketClientProtocolHandler(handShaker));
        // 自定义的handler
        pipeline.addLast(new XiaoFeiImHandler(processor));
    }
}
