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

import java.util.function.Consumer;

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
        Packet errorRespPacket = this.buildPacket(Command.ERROR, ProtocolConst.SerializedClass.ERROR_RESP, packet, (ErrorResp errorResp) -> {
            errorResp.setErrorCode(errorCode.getErrorCode())
                .setReason(reason);
        });
        session.write(errorRespPacket);
    }
    
    protected <T extends Msg> Packet buildPacket(Command command, ProtocolConst.SerializedClass serializedClass, Packet packet, Consumer<T> consumer){
        byte serializerType = packet.getSerializerType();
        byte classKey = serializedClass.getClassKey();
        Packet respPacket = Packet.create(command);
        respPacket.setSerializerType(serializerType);
        respPacket.setClassKey(classKey);
        respPacket.setSequenceNum(packet.getSequenceNum() + 1);
        T msg = serializedClassManager.newObject(serializerType, classKey);
        consumer.accept(msg);
        packet.setData(serializedClassManager.getSerializedObject(serializerType, msg));
        return respPacket;
    }
  
}
