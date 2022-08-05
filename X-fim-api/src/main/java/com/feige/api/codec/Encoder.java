package com.feige.api.codec;

public interface Encoder {

    IByteBuf encode(Object obj);
}
