package com.feige.fim.serialize;

import com.feige.annotation.SpiComp;
import com.feige.api.msg.Msg;
import com.feige.api.serialize.Serializer;
import com.feige.api.constant.ProtocolConst;

@SpiComp
public class ProtoBufSerializer implements Serializer {

    @Override
    public byte[] serialize(Msg obj) {
        return obj.serialize();
    }

    @Override
    public Object deserialize(Class<?> clazz, byte[] bytes) {
        Object obj = null;
        try {
            obj = clazz.newInstance();
            if (obj instanceof Msg){
                ((Msg) obj).deserialize(bytes);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    @Override
    public byte getType() {
        return ProtocolConst.PROTOCOL_BUFFER;
    }
}
