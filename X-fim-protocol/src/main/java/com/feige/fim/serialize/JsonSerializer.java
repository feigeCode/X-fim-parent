package com.feige.fim.serialize;

import com.feige.api.constant.ProtocolConst;
import com.feige.api.serialize.Serializer;
import com.feige.utils.json.JsonUtils;
import com.feige.utils.spi.annotation.SpiComp;

@SpiComp(interfaces = Serializer.class)
public class JsonSerializer implements Serializer {
    
    @Override
    public byte[] serialize(Object obj) {
        return JsonUtils.toJsonBytes(obj);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JsonUtils.fromJson(bytes, clazz);
    }

    @Override
    public byte getType() {
        return ProtocolConst.JSON;
    }
}
