package com.feige.im.parser.impl;


import com.feige.im.codec.ProtocolHeadLenType;
import com.feige.im.parser.AbstractParser;
import io.netty.buffer.ByteBuf;

public class XmlParser extends AbstractParser {

    @Override
    public Object decode(ByteBuf buf, ProtocolHeadLenType protocolHeadLenType, int msgLength, int key) {
        return null;
    }

    @Override
    public ByteBuf encode(Object msg, ProtocolHeadLenType protocolHeadLenType, int key) {
        return null;
    }

    @Override
    public int getType() {
        return 0;
    }
}
