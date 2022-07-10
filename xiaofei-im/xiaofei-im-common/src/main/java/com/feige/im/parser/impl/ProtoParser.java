package com.feige.im.parser.impl;

import com.feige.im.codec.ProtocolHeadLenType;
import com.feige.im.parser.AbstractParser;
import com.feige.im.parser.Parser;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;

import java.util.Optional;

public class ProtoParser extends AbstractParser {


    @Override
    public Object decode(ByteBuf buf, ProtocolHeadLenType protocolHeadLenType, int msgLength, int key) {
        // 读取内容
        byte[] content = new byte[msgLength];
        buf.readBytes(content);
        return Parser.getMessage(key, content);

    }

    @Override
    public ByteBuf encode(Object msg, ProtocolHeadLenType protocolHeadLenType, int key) {
        if (msg instanceof Message){
            Message message = (Message) msg;
            byte[] body = message.toByteArray();
            // 申请buffer
            ByteBuf buf = getBuf(protocolHeadLenType, body.length);
            // 写入消息Key
            buf.writeInt(key);
            // 写入消息体
            buf.writeBytes(body);
            return buf;
        }
        return null;
    }

    @Override
    public int getType() {
        return 1;
    }
}
