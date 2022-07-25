package com.feige.im.codec;

import com.feige.im.constant.ImConst;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.parser.IParser;
import com.feige.im.parser.Parser;
import com.feige.im.parser.ParserManager;
import com.feige.im.utils.StringUtil;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author feige<br />
 * @ClassName: MsgCodecUtil <br/>
 * @Description: <br/>
 * @date: 2022/3/14 17:58<br/>
 */
public class MsgCodecUtil {

    private static final Logger LOG = LoggerFactory.getLogger();

    public static void msgDecoder(ByteBuf buf, List<Object> out, int msgLength, ProtocolHeadLenType protocolHeadLenType){
        // 解析器类型
        int parserKey = buf.readInt();

        // 消息的key
        int key = buf.readInt();

        // 心跳
        if (parserKey == -1 && ImConst.PONG_MSG_TYPE == key){
            LOG.debugInfo("收到心跳{}", StringUtil.printMsg(ImConst.PONG_MSG));
            out.add(ImConst.PONG_MSG);
            return;
        }

        // 集群模式下，客户端发送心跳需要
        if (parserKey == -1 && ImConst.PING_MSG_TYPE == key){
            out.add(ImConst.PING_MSG);
            return;
        }

        IParser parser = ParserManager.getParser(parserKey);
        if (parser == null){
            buf.resetReaderIndex();
            LOG.error("unknown parser type: {}", parserKey);
            return;
        }

        // 解析消息
        Object message = parser.decode(buf, protocolHeadLenType, msgLength == -1 ? buf.readableBytes() : msgLength , key);
        if (!Objects.isNull(message)){
            LOG.debugInfo("received message content length = {}, msgKey = {}", msgLength, key);
            out.add(message);
        }
    }

    public static void msgEncoder(ByteBuf buf, Object msg, ProtocolHeadLenType protocolHeadLenType) {
        try {
            Optional<Parser.ParserKey> keyOp = Optional.of(Parser.getKey(msg));
            int uid = keyOp
                    .map(Parser.ParserKey::getUid)
                    .orElseThrow(NullPointerException::new);
            ByteBuf buffer;
            if (ImConst.PONG_MSG_TYPE == uid || ImConst.PING_MSG_TYPE == uid){
                boolean isPong = ImConst.PONG_MSG_TYPE == uid;
                Message heartbeatMsg = isPong ? ImConst.PONG_MSG : ImConst.PING_MSG;
                byte[] bytes = heartbeatMsg.toByteArray();
                buffer = Unpooled.buffer(protocolHeadLenType.getLength());
                buffer.writeInt(bytes.length);
                buffer.writeInt(-1);
                buffer.writeInt(isPong ? ImConst.PONG_MSG_TYPE : ImConst.PING_MSG_TYPE);
                buffer.writeBytes(bytes);
            }else {
                int parserType = keyOp
                        .map(Parser.ParserKey::getParserType)
                        .orElseThrow(NullPointerException::new);
                IParser parser = ParserManager.getParser(parserType);
                if (parser == null){
                    LOG.error("unknown parser type: {}", parserType);
                    return;
                }
                buffer = parser.encode(msg, protocolHeadLenType, uid);
            }
            if (buffer != null){
                if (protocolHeadLenType == ProtocolHeadLenType.TCP){
                    buf.writeBytes(buffer);
                }else if (protocolHeadLenType == ProtocolHeadLenType.WS){
                    buf.writeBytes(new BinaryWebSocketFrame(buffer).content());
                }
            } else {
                LOG.warn("buffer is null, protocol is {}", protocolHeadLenType.name());
            }
        }catch (Exception e){
            LOG.error("error: ", e);
        }
    }
}
