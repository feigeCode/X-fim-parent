package com.feige.fim.serialize;

import com.feige.api.annotation.SpiComp;
import com.feige.api.msg.Msg;
import com.feige.api.serialize.Serializer;
import com.feige.fim.protocol.ProtocolConst;

@SpiComp
public class ProtoBufSerializer implements Serializer {

    @Override
    public byte[] serialize(Msg obj) {
        return obj.serialize();
    }

    @Override
    public <T extends Msg> T deserialize(Class<T> clazz, byte[] bytes) {
        T t = null;
        try {
            t = clazz.newInstance();
            t.deserialize(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    @Override
    public byte getType() {
        return ProtocolConst.PROTOCOL_BUFFER;
    }
}
