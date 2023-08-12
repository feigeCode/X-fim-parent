package com.feige.fim.serialize;

import com.feige.api.constant.ProtocolConst;
import com.feige.api.serialize.SerializedClassManager;
import com.feige.api.serialize.Serializer;
import com.feige.framework.annotation.Inject;
import com.feige.framework.annotation.SpiComp;
import com.feige.framework.api.context.InitializingInstance;

@SpiComp(interfaces = Serializer.class)
public class JsonSerializer implements Serializer, InitializingInstance {

    @Inject
    private SerializedClassManager serializedClassManager;
    
    @Override
    public byte[] serialize(Object obj) {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return null;
    }

    @Override
    public byte getType() {
        return ProtocolConst.JSON;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new JsonSerializedClassInit(serializedClassManager).initialize();
    }
}
