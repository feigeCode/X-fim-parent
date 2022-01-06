package com.feige.im.codec;

import com.feige.im.constant.ImConst;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.DefaultMsg;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author feige<br />
 * @ClassName: XiaoFeiProtoBufEncoder <br/>
 * @Description: proto 编码器<br/>
 * @date: 2021/10/7 12:35<br/>
 */
@ChannelHandler.Sharable
public class XiaoFeiProtoBufEncoder extends MessageToByteEncoder<Message> {

    private static final Logger LOG = LoggerFactory.getLogger();
    public static XiaoFeiProtoBufEncoder INSTANCE = new XiaoFeiProtoBufEncoder();

    private XiaoFeiProtoBufEncoder(){

    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf buf) throws Exception {

        try {
            byte[] bytes = msg.toByteArray();
            int msgLength = bytes.length;

            // 申请buffer
            ByteBuf buffer = ctx.channel().config().getAllocator().buffer(msgLength + 8);

            // 写入消息长度
            buffer.writeInt(msgLength);

            // 写入消息key
            if (msg instanceof DefaultMsg.Pong){
                buffer.writeInt(ImConst.PONG_MSG_TYPE);
            } else if (msg instanceof DefaultMsg.Ping){
                buffer.writeInt(ImConst.PING_MSG_TYPE);
            }else {
                Integer key = Parser.getKey(msg.getClass());
                buffer.writeInt(key);
            }
            // 写入消息
            buffer.writeBytes(bytes);

            // 写入buffer
            buf.writeBytes(buffer);
        } catch (Exception e) {
            LOG.error("msg encode error:",e);
        }
    }
}
