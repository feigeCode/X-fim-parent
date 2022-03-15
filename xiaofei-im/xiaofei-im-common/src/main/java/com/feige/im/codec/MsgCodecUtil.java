package com.feige.im.codec;

import com.feige.im.constant.ImConst;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.HeartBeat;
import com.feige.im.utils.StringUtil;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;

import java.util.List;
import java.util.Objects;

/**
 * @author feige<br />
 * @ClassName: MsgCodecUtil <br/>
 * @Description: <br/>
 * @date: 2022/3/14 17:58<br/>
 */
public class MsgCodecUtil {

    private static final Logger LOG = LoggerFactory.getLogger();

    public static void msgDecoder(ByteBuf buf, List<Object> out, int msgLength){
        // 消息的key
        int key = buf.readInt();

        // 读取内容
        byte[] content = new byte[msgLength == -1 ? buf.readableBytes() : msgLength];
        buf.readBytes(content);

        // 心跳
        if (ImConst.PONG_MSG_TYPE == key){
            LOG.debugInfo("收到心跳{}", StringUtil.protoMsgFormat(ImConst.PONG_MSG));
            out.add(ImConst.PONG_MSG);
            return;
        }

        // 集群模式下，客户端发送心跳需要
        if (ImConst.PING_MSG_TYPE == key){
            out.add(ImConst.PING_MSG);
            return;
        }

        // 解析消息
        Message message = Parser.getMessage(key,content);
        if (!Objects.isNull(message)){
            LOG.debugInfo("received message content length = {}, msgKey = {}", msgLength, key);
            out.add(message);
        }
    }

    public static void msgEncoder(ByteBuf buffer, Message msg) throws Exception{
        try {
            // 写入消息key
            if (msg instanceof HeartBeat.Pong){
                buffer.writeInt(ImConst.PONG_MSG_TYPE);
            } else if (msg instanceof HeartBeat.Ping){
                buffer.writeInt(ImConst.PING_MSG_TYPE);
            }else {
                Integer key = Parser.getKey(msg.getClass());
                buffer.writeInt(key);
            }
        }catch (Exception e){
            throw new Exception(e);
        }
    }
}
