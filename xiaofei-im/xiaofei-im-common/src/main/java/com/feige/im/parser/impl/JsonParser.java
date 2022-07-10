package com.feige.im.parser.impl;

import com.feige.im.codec.ProtocolHeadLenType;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.parser.AbstractParser;
import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;


public class JsonParser extends AbstractParser {

    private static final Logger LOG =  LoggerFactory.getLogger();
    public static final Gson gson = new Gson();


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
        return 2;
    }

}
