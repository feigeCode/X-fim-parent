package com.feige.api.codec;

import com.feige.api.session.ISession;

public interface Codec {

    /**
     * 编码
     * @param session
     * @param byteBuf
     * @param obj
     * @return
     */
    IByteBuf encode(ISession session, IByteBuf byteBuf, Object obj);

    /**
     * 解码
     * @param session
     * @param byteBuf
     * @return
     */
    Object decode(ISession session, IByteBuf byteBuf);

    /**
     * 编解密器key
     * @return
     */
    byte getCodecKey();

}
