package com.feige.im.server;

import com.feige.im.codec.ws.XiaoFeiWsMsgDecoder;
import com.feige.im.codec.ws.XiaoFeiWsMsgEncoder;
import com.feige.im.handler.MsgListener;
import com.feige.im.handler.ServerHeartbeatHandler;
import com.feige.im.handler.WsAuthenticateHandler;
import com.feige.im.handler.XiaoFeiImHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author feige<br />
 * @ClassName: WsServerInitializer <br/>
 * @Description: <br/>
 * @date: 2022/3/14 16:45<br/>
 */
public class WsServerInitializer extends ChannelInitializer<SocketChannel> {


    private final MsgListener listener;
    private final String wsPath;

    public WsServerInitializer(MsgListener listener, String wsPath){
        this.listener = listener;
        this.wsPath = wsPath;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //基于http协议，所以要有http编码解码器
        pipeline.addLast(new HttpServerCodec());
        //对写大数据的支持
        pipeline.addLast(new ChunkedWriteHandler());
        //对httpMessage进行聚合，聚合成FullHttpRequest或FullHttpResponse
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));
        //==============以上是用于支持http协议=============
        //websocket服务器处理的协议，用于指定给客户端连接访问的路由：/ws
        //本handler会帮你处理一些繁重的的复杂的事
        //会帮你处理握手动作：handshaking(close,ping,pong)ping + pong = 心跳
        //对于websocket来讲，都是以frames进行传输的，不同的数据类型对应的frames也不同
        pipeline.addLast(new WebSocketServerProtocolHandler(wsPath, null, true, 65536, false, true));

        // ws认证
        pipeline.addLast(new WsAuthenticateHandler(listener));

        // 自定义消息编解码
        pipeline.addLast(new XiaoFeiWsMsgEncoder());
        pipeline.addLast(new XiaoFeiWsMsgDecoder());

        // 心跳
        pipeline.addLast(new IdleStateHandler(45,60,0, TimeUnit.SECONDS));
        pipeline.addLast(new ServerHeartbeatHandler());
        // 自定义的handler
        pipeline.addLast(new XiaoFeiImHandler(listener));


    }
}
