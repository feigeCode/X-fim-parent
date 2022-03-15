package com.feige.im.codec.ws;

import com.feige.im.codec.MsgCodecUtil;
import com.feige.im.constant.ChannelAttr;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.List;

/**
 * @author feige<br />
 * @ClassName: XiaoFeiWsMsgDecoder <br/>
 * @Description: <br/>
 * @date: 2022/3/14 17:54<br/>
 */
public class XiaoFeiWsMsgDecoder extends MessageToMessageDecoder<BinaryWebSocketFrame> {

    @Override
    protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame msg, List<Object> out) throws Exception {
        // 重置心跳 超时次数
        ctx.channel().attr(ChannelAttr.PING_COUNT).set(null);

        ByteBuf buf = msg.content();
        // 标记当前读取的索引
        buf.markReaderIndex();

        // 判断是否能够读取指定长度
        if (buf.readableBytes() < 4){
            buf.resetReaderIndex();
            return;
        }

        MsgCodecUtil.msgDecoder(buf, out, -1);
    }
}
