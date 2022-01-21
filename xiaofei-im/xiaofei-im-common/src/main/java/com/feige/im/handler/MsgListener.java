package com.feige.im.handler;

import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author feige<br />
 * @ClassName: MsgListener <br/>
 * @Description: <br/>
 * @date: 2022/1/21 13:51<br/>
 */
public interface MsgListener {

    /**
     *上线
     * @param ctx Channel上下文
     */
    void active(ChannelHandlerContext ctx);


    /**
     *收到消息
     * @param ctx Channel上下文
     * @param msg 收到的消息
     */
    void read(ChannelHandlerContext ctx, Message msg);

    /**
     *下线
     * @param ctx Channel上下文
     */
    void inactive(ChannelHandlerContext ctx);

    /**
     * 发生异常
     * @param ctx Channel上下文
     * @param cause 异常
     */
    void exceptionCaught(ChannelHandlerContext ctx, Throwable cause);
}
