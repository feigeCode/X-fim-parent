package com.feige.api.codec;

import com.feige.api.session.ISession;

public interface Encoder {

    /**
     * 编码
     * @param session
     * @param byteBuf
     * @param obj
     * @return
     */
    IByteBuf encode(ISession session, IByteBuf byteBuf, Object obj);
}
