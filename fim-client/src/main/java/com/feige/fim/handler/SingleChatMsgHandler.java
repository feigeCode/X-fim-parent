package com.feige.fim.handler;

import com.feige.api.constant.Command;
import com.feige.api.constant.ProtocolConst;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.msg.Ack;
import com.feige.api.msg.ChatMsgReq;
import com.feige.api.session.Session;
import com.feige.fim.protocol.Packet;
import com.feige.utils.spi.annotation.SPI;

@SPI(interfaces = MsgHandler.class)
public class SingleChatMsgHandler extends AbstractMsgHandler{
    @Override
    public byte getCmd() {
        return Command.SINGLE_CHAT.getCmd();
    }

    @Override
    public void handle(Session session, Packet msg) throws RemotingException {
        ChatMsgReq chatMsgReq = this.getMsg(msg, ChatMsgReq.class);
        System.out.println(chatMsgReq.getContent());
        if (msg.hasFeature(ProtocolConst.AUTO_ACK) || msg.hasFeature(ProtocolConst.BIZ_ACK)){
            Packet packet = this.buildPacket(Command.ACK, ProtocolConst.SerializedClass.ACK, msg, (Ack ack) -> {
                ack.setMsgId(chatMsgReq.getId())
                        .setSendTime(System.currentTimeMillis())
                        .setSeqId(msg.getSeqId())
                        .setSenderId(chatMsgReq.getReceiverId())
                        .setReceiverId(chatMsgReq.getSenderId())
                        .setExtra("1");

            });
            session.write(packet);
        }

    }
}
