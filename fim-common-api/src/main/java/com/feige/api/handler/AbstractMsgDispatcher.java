package com.feige.api.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractMsgDispatcher<T> implements MsgDispatcher<T> {
    
    protected final Map<Byte, MsgHandler>  msgHandlerMap = new ConcurrentHashMap<>();

    @Override
    public void register(MsgHandler msgHandler) {
        if (msgHandler != null){
            byte cmd = msgHandler.getCmd();
            MsgHandler handler = getMsgHandler(cmd);
            if (handler != null){
                throw new RuntimeException("exists cmd" + cmd);
            }
            msgHandlerMap.put(msgHandler.getCmd(), msgHandler);
        }
    }

    @Override
    public MsgHandler unregister(byte cmd) {
        return msgHandlerMap.remove(cmd);
    }

    @Override
    public MsgHandler getMsgHandler(byte cmd) {
        return msgHandlerMap.get(cmd);
    }
}
