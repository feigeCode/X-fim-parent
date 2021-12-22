package com.feige.im.codec;

import com.feige.im.constant.ImConst;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.DefaultMsg;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;


import java.util.List;

/**
 * @author feige<br />
 * @ClassName: XiaoFeiProtoBufEncoder <br/>
 * @Description: proto 编码器<br/>
 * @date: 2021/10/7 12:35<br/>
 */
@ChannelHandler.Sharable
public class XiaoFeiProtoBufEncoder extends MessageToMessageEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        byte[] bytes = msg.toByteArray();
        ByteBuf buffer = ctx.channel().config().getAllocator().buffer(bytes.length + 1);
        if (msg instanceof DefaultMsg.Pong){
            buffer.writeByte(ImConst.PONG_MSG_TYPE);
        } else if (msg instanceof DefaultMsg.Ping){
            buffer.writeByte(ImConst.PING_MSG_TYPE);
        }else {
            Integer key = Parser.getKey(msg.getClass());
            buffer.writeByte(key);
        }
        buffer.writeBytes(bytes);
        out.add(new BinaryWebSocketFrame(buffer));
    }
}
