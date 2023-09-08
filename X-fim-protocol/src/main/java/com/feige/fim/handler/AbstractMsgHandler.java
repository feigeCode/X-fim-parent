package com.feige.fim.handler;


import com.feige.api.constant.Command;
import com.feige.api.constant.ProtocolConst;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.msg.ErrorResp;
import com.feige.api.msg.Msg;
import com.feige.api.session.Session;
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


    protected void sendErrorPacket(Session session, Packet packet, ProtocolConst.ErrorCode errorCode, String reason) throws RemotingException {
        byte serializerType = packet.getSerializerType();
        ErrorResp errorResp = this.serializedClassManager.newObject(serializerType, ErrorResp.TYPE);
        errorResp.setErrorCode(errorCode.getErrorCode());
        errorResp.setReason(reason);
        Packet packetResp = Packet.create(Command.ERROR);
        packetResp.setSequenceNum(packet.getSequenceNum() + 1);
        packetResp.setSerializerType(serializerType);
        packetResp.setClassKey(ProtocolConst.SerializedClass.ERROR_RESP.getClassKey());
        packetResp.setData(serializedClassManager.getSerializedObject(serializerType, errorResp));
        session.write(errorResp);
    }
  
}
