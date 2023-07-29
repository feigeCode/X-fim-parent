package com.feige.fim.handler;

import com.feige.framework.annotation.SpiComp;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.msg.Handshake;
import com.feige.api.constant.Command;
import com.feige.api.handler.AbstractMsgHandler;
import com.feige.api.session.Session;
import com.feige.fim.msg.proto.HandshakeMsgProto;
import com.feige.fim.protocol.Packet;
import com.google.auto.service.AutoService;

/**
 * @author feige<br />
 * @ClassName: HandshakeMsgHandler <br/>
 * @Description: <br/>
 * @date: 2023/5/25 21:52<br/>
 */
@SpiComp("handshake")
@AutoService(MsgHandler.class)
public class HandshakeMsgHandler extends AbstractMsgHandler<Packet> {
    
    @Override
    public byte getCmd() {
        return Command.HANDSHAKE.getCmd();
    }

    @Override
    public void handle(Session session, Packet packet) throws RemotingException {
        Handshake handshake = serializedClassManager.getDeserializedObject(packet.getSerializerType(), packet.getClassKey(), packet.getData(), getMsgInterface());
        
    }


    @Override
    public  Class<Handshake> getMsgInterface() {
        return Handshake.class;
    }

    @Override
    public Class<?> getProtoClass() {
        return HandshakeMsgProto.class;
    }
}
