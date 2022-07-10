package com.feige.im.codec.ws;

import com.feige.im.codec.MsgCodecUtil;
import com.feige.im.codec.ProtocolHeadLenType;
import com.feige.im.constant.ImConst;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.HeartBeat;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.List;

/**
 * @author feige<br />
 * @ClassName: XiaoFeiWsMsgEncoder <br/>
 * @Description: <br/>
 * @date: 2022/3/14 17:54<br/>
 */
public class XiaoFeiWsMsgEncoder extends MessageToByteEncoder<Object> {

    private static final Logger LOG = LoggerFactory.getLogger();

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf buf) throws Exception {
        MsgCodecUtil.msgEncoder(buf, msg, ProtocolHeadLenType.WS);
    }
}
