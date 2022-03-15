package com.feige.im.codec.tcp;

import com.feige.im.codec.MsgCodecUtil;
import com.feige.im.constant.ChannelAttr;
import com.feige.im.constant.ImConst;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.parser.Parser;
import com.feige.im.utils.StringUtil;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.Objects;

/**
 * @author feige<br />
 * @ClassName: XiaoFeiTcpProtoBufDecoder <br/>
 * @Description: proto 解码器<br/>
 * @date: 2021/10/7 12:35<br/>
 */
public class XiaoFeiTcpProtoBufDecoder extends ByteToMessageDecoder {
    private static final Logger LOG = LoggerFactory.getLogger();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        // 重置心跳 超时次数
        ctx.channel().attr(ChannelAttr.PING_COUNT).set(null);

        // 标记当前读取的索引
        buf.markReaderIndex();

        // 判断是否能够读取指定长度
        if (buf.readableBytes() < 4){
            buf.resetReaderIndex();
            return;
        }

        // 读取消息长度
        int msgLength = buf.readInt();
        if (msgLength < 0){
            LOG.error("negative length: {}", msgLength);
            ctx.close();
            return;
        }

        // 剩下可读消息长度减去消息key小于需读消息长度，则退回标记的位置
        if (buf.readableBytes() - 4 < msgLength){
            buf.resetReaderIndex();
            return;
        }

        MsgCodecUtil.msgDecoder(buf, out, msgLength);
    }
}
