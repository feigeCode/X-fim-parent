package com.feige.api.serialize;

import com.feige.api.msg.MsgFactory;

public class MsgGen {
    private Class<?> msgClass;
    
    private Class<MsgFactory> msgFactory;

    public MsgGen() {
    }

    public MsgGen(Class<?> msgClass, Class<MsgFactory> msgFactory) {
        this.msgClass = msgClass;
        this.msgFactory = msgFactory;
    }

    public Class<?> getMsgClass() {
        return msgClass;
    }

    public void setMsgClass(Class<?> msgClass) {
        this.msgClass = msgClass;
    }

    public Class<MsgFactory> getMsgFactory() {
        return msgFactory;
    }

    public void setMsgFactory(Class<MsgFactory> msgFactory) {
        this.msgFactory = msgFactory;
    }

    @Override
    public String toString() {
        return "MsgGen{" +
                "msgClass=" + msgClass +
                ", msgFactory=" + msgFactory +
                '}';
    }
}
