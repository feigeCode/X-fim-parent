package com.feige.api.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractMsgDispatcher<T> implements MsgDispatcher<T> {
    
    protected final Map<Byte, MsgHandler<T>>  msgHandlerMap = new ConcurrentHashMap<>();

    @Override
    public void register(MsgHandler<T> msgHandler) {
        if (msgHandler != null){
            byte cmd = msgHandler.getCmd();
            MsgHandler<T> handler = getMsgHandler(cmd);
            if (handler != null){
                throw new RuntimeException("exists cmd" + cmd);
            }
            msgHandlerMap.put(msgHandler.getCmd(), msgHandler);
        }
    }

    @Override
    public MsgHandler<T> unregister(byte cmd) {
        return msgHandlerMap.remove(cmd);
    }

    @Override
    public MsgHandler<T> getMsgHandler(byte cmd) {
        return msgHandlerMap.get(cmd);
    }
}
