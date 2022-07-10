package com.feige.im.codec.udp;

import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @author feige<br />
 * @ClassName: XiaoFeiUdpMsgEncoder <br/>
 * @Description: <br/>
 * @date: 2022/3/15 0:38<br/>
 */
public class XiaoFeiUdpMsgEncoder extends MessageToMessageEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        // TODO 等待实现
    }
}
