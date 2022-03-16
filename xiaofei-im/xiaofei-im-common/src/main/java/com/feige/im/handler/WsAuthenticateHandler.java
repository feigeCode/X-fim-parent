package com.feige.im.handler;

import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.parser.Parser;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * @author feige<br />
 * @ClassName: WsAuthenticateHandler <br/>
 * @Description: <br/>
 * @date: 2022/3/15 16:22<br/>
 */
public class WsAuthenticateHandler extends ChannelInboundHandlerAdapter {


    private static final Logger LOG = LoggerFactory.getLogger();

    private final MsgListener msgListener;

    public WsAuthenticateHandler(MsgListener msgListener){
        this.msgListener = msgListener;
    }



    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            WebSocketServerProtocolHandler.HandshakeComplete event = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            Parser.WsAuthMsgConverter converter = Parser.WS_AUTH_MSG_CONVERTER;
            if (converter != null){
                msgListener.read(ctx, converter.convert(event));
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
