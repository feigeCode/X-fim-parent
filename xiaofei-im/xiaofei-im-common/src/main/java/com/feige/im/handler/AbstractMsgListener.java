package com.feige.im.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public abstract class AbstractMsgListener implements MsgListener{

    @Override
    public void onReceive(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest){
            this.onReceivedRequest(ctx, (FullHttpRequest) msg);
        }else {
            this.onReceivedMsg(ctx, msg);
        }
    }
    /**
     * 接收消息处理
     * @param ctx 上下文
     * @param msg 消息
     */
    public abstract void onReceivedMsg(ChannelHandlerContext ctx, Object msg);


    /**
     * 接收请求处理
     * @param ctx 上下文
     * @param req 请求
     */
    public abstract void onReceivedRequest(ChannelHandlerContext ctx, FullHttpRequest req);
}
