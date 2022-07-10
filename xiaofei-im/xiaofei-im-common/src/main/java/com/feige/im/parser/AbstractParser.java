package com.feige.im.parser;

import com.feige.im.codec.ProtocolHeadLenType;
import com.feige.im.constant.ImConst;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public abstract class AbstractParser implements IParser{

    protected static final Logger LOG = LoggerFactory.getLogger();

    @Override
    public <T> T getObject(Class<T> clazz, Object msg){
        if (clazz.isInstance(msg)){
            return clazz.cast(msg);
        }
        return null;
    }

    /**
     * 申请Buffer
     * @param protocolHeadLenType 协议头长度枚举
     * @param bodyLength 消息长度
     * @return
     */
    public ByteBuf getBuf(ProtocolHeadLenType protocolHeadLenType, int bodyLength){
        ByteBuf buf = Unpooled.buffer(protocolHeadLenType.getLength() + bodyLength);
        if (protocolHeadLenType == ProtocolHeadLenType.TCP){
            // 写入消息长度
            buf.writeInt(bodyLength);
        }
        // 写入消息解析器
        buf.writeInt(this.getType());
        return buf;
    }



}
