package com.feige.fim.handler;


import com.feige.api.handler.MsgHandler;
import com.feige.api.msg.Msg;
import com.feige.fim.protocol.Packet;
import com.feige.framework.annotation.Inject;

import com.feige.api.serialize.SerializedClassManager;

/**
 * @author feige<br />
 * @ClassName: AbstractMsgHandler <br/>
 * @Description: <br/>
 * @date: 2023/5/25 21:50<br/>
 */
public abstract class AbstractMsgHandler implements MsgHandler<Packet> {
    

    
    @Inject
    protected SerializedClassManager serializedClassManager;
    
    
    protected <R extends Msg> R getMsg(Packet packet, Class<R> msgInterface){
        byte serializerType = packet.getSerializerType();
        byte classKey = packet.getClassKey();
        byte[] data = packet.getData();
        return serializedClassManager.getDeserializedObject(serializerType, classKey, data, msgInterface);
    }
  
}
