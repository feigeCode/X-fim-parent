package com.feige.im.codec;

import com.feige.im.constant.ImConst;

public enum ProtocolHeadLenType {
    TCP(ImConst.TCP_MSG_HEAD_LENGTH),
    UDP(ImConst.WS_MSG_HEAD_LENGTH),
    WS(ImConst.UDP_MSG_HEAD_LENGTH);


    private int length;

    ProtocolHeadLenType(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
