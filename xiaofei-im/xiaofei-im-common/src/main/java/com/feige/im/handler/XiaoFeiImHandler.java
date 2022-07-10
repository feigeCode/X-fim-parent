package com.feige.im.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;


/**
 * @author feige<br />
 * @ClassName: XiaoFeiImHandler <br/>
 * @Description: <br/>
 * @date: 2021/10/7 14:46<br/>
 */
@ChannelHandler.Sharable
public class XiaoFeiImHandler extends ChannelInboundHandlerAdapter {

    private final MsgListener msgListener;

    public XiaoFeiImHandler(MsgListener msgListener){
        this.msgListener = msgListener;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            msgListener.onReceive(ctx, msg);
        }catch (Throwable throwable){
            msgListener.exceptionCaught(ctx, throwable);
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        msgListener.onActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        msgListener.onInactive(ctx);

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        msgListener.exceptionCaught(ctx,cause);
    }
}
