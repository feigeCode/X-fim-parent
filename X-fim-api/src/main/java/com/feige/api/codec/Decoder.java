package com.feige.api.codec;

public interface Decoder {

    Object decode(IByteBuf byteBuf);

}
