package com.feige.im.codec.udp;


import com.feige.im.codec.MsgCodecUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @author feige<br />
 * @ClassName: XiaoFeiUdpMsgDecoder <br/>
 * @Description: <br/>
 * @date: 2022/3/15 0:38<br/>
 */
public class XiaoFeiUdpMsgDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
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
