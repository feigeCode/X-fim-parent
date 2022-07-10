package com.feige.im.codec.tcp;

import com.feige.im.codec.MsgCodecUtil;
import com.feige.im.codec.ProtocolHeadLenType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author feige<br />
 * @ClassName: XiaoFeiTcpMsgEncoder <br/>
 * @Description: proto 编码器<br/>
 * @date: 2021/10/7 12:35<br/>
 */
public class XiaoFeiTcpMsgEncoder extends MessageToByteEncoder<Object> {


    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf buf) throws Exception {
        MsgCodecUtil.msgEncoder(buf, msg, ProtocolHeadLenType.TCP);
    }
}
