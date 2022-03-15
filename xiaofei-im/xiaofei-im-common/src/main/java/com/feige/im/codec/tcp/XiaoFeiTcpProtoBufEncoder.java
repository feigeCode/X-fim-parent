package com.feige.im.codec.tcp;

import com.feige.im.codec.MsgCodecUtil;
import com.feige.im.constant.ImConst;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.HeartBeat;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author feige<br />
 * @ClassName: XiaoFeiTcpProtoBufEncoder <br/>
 * @Description: proto 编码器<br/>
 * @date: 2021/10/7 12:35<br/>
 */
public class XiaoFeiTcpProtoBufEncoder extends MessageToByteEncoder<Message> {

    private static final Logger LOG = LoggerFactory.getLogger();


    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf buf) throws Exception {

        try {

            byte[] bytes = msg.toByteArray();
            int msgLength = bytes.length;


            // 申请buffer
            ByteBuf buffer = ctx.alloc().buffer(msgLength + 8);

            // 写入消息长度
            buffer.writeInt(msgLength);

            // 写入消息key
            MsgCodecUtil.msgEncoder(buffer, msg);

            // 写入消息
            buffer.writeBytes(bytes);

            // 写入buffer
            buf.writeBytes(buffer);
        } catch (Exception e) {
            LOG.error("msg encode error:",e);
        }
    }
}
