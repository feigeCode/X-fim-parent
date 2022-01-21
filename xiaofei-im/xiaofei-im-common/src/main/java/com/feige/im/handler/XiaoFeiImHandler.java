package com.feige.im.handler;

import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


/**
 * @author feige<br />
 * @ClassName: XiaoFeiImHandler <br/>
 * @Description: <br/>
 * @date: 2021/10/7 14:46<br/>
 */
@ChannelHandler.Sharable
public class XiaoFeiImHandler extends SimpleChannelInboundHandler<Message> {

    private final MsgListener msgListener;

    public XiaoFeiImHandler(MsgListener msgListener){
        this.msgListener = msgListener;
    }

    private static final Logger LOG = LoggerFactory.getLogger();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        msgListener.read(ctx,msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        msgListener.active(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        msgListener.inactive(ctx);

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        msgListener.exceptionCaught(ctx,cause);
    }
}
