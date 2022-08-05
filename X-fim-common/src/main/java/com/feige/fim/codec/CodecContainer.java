package com.feige.fim.codec;


import com.feige.api.codec.Codec;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CodecContainer {

    public static final Map<Byte, Codec> codecMap = new ConcurrentHashMap<>();


    public void register(byte key, Codec value){
        codecMap.put(key, value);
    }

    public Codec unRegister(byte key) {
        return codecMap.remove(key);
    }


    public Codec getCodec(byte key){
        return codecMap.get(key);
    }


}
