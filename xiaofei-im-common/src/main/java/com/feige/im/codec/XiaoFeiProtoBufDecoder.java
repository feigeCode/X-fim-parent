package com.feige.im.codec;

import com.feige.im.constant.ChannelAttr;
import com.feige.im.constant.ImConst;
import com.feige.im.parser.Parser;
import com.feige.im.utils.StringUtil;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;

/**
 * @author feige<br />
 * @ClassName: XiaoFeiProtoBufDecoder <br/>
 * @Description: proto 解码器<br/>
 * @date: 2021/10/7 12:35<br/>
 */
@ChannelHandler.Sharable
public class XiaoFeiProtoBufDecoder extends MessageToMessageDecoder<BinaryWebSocketFrame> {
    private static final Logger LOG = LogManager.getLogger(XiaoFeiProtoBufDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame msg, List<Object> out) throws Exception {
        ctx.channel().attr(ChannelAttr.PING_COUNT).set(null);
        ByteBuf buf = msg.content();
        byte type = buf.readByte();
        // 心跳
        if (ImConst.PONG_MSG_TYPE == type){
            LOG.info("收到心跳{}",() -> StringUtil.protoMsgFormat(ImConst.PONG_MSG));
            out.add(ImConst.PONG_MSG);
            return;
        }
        // 集群模式下，客户端发送心跳需要
        if (ImConst.PING_MSG_TYPE == type){
            out.add(ImConst.PING_MSG);
            return;
        }
        Message message = Parser.getMessage((int) type,new ByteBufInputStream(buf));
        if (!Objects.isNull(message)){
            out.add(message);
        }
    }
}
