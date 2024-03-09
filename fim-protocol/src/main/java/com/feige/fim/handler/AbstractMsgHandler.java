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
public abstract class AbstractMsgHandler implements MsgHandler {
    

    
    @Inject
    protected SerializedClassManager serializedClassManager;

    @Override
    public void handle(Session session, Object msg) throws RemotingException {
        if (msg instanceof Packet){
            handle(session, (Packet) msg);
        }
    }

    public abstract void handle(Session session, Packet msg) throws RemotingException;



    protected <R extends Msg> R getMsg(Packet packet, Class<R> msgInterface){
        byte serializerType = packet.getSerializer();
        byte classKey = packet.getRealType();
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
        byte serializerType = packet.getSerializer();
        byte classKey = serializedClass.getClassKey();
        Packet respPacket = Packet.create(command);
        respPacket.setSerializer(serializerType);
        respPacket.setRealType(classKey);
        respPacket.setSeqId(packet.getSeqId() + 1);
        T msg = serializedClassManager.newObject(serializerType, classKey);
        consumer.accept(msg);
        respPacket.setData(serializedClassManager.getSerializedObject(serializerType, msg));
        return respPacket;
    }
  
}
