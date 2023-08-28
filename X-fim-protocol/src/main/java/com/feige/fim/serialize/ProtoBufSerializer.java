package com.feige.fim.serialize;

import com.feige.api.serialize.SerializedClassManager;
import com.feige.framework.annotation.Inject;
import com.feige.utils.spi.annotation.SpiComp;
import com.feige.api.msg.Msg;
import com.feige.api.serialize.Serializer;
import com.feige.api.constant.ProtocolConst;
import com.feige.framework.api.context.InitializingComp;


@SpiComp(interfaces = Serializer.class)
public class ProtoBufSerializer implements Serializer, InitializingComp {

    @Inject
    private SerializedClassManager serializedClassManager;
    
    @Override
    public byte[] serialize(Object obj) {
        if (obj instanceof Msg){
            return ((Msg) obj).serialize();
        }
        return new byte[0];
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        T obj = null;
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

    @Override
    public void afterPropertiesSet() throws Exception {
        new ProtoBufSerializedClassInit(serializedClassManager).initialize();
    }
}
