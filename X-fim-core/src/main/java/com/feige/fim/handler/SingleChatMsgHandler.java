package com.feige.fim.handler;

import com.feige.api.constant.Command;
import com.feige.api.constant.ProtocolConst;
import com.feige.api.constant.ProtocolConst.SerializedClass;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.msg.Ack;
import com.feige.api.msg.ChatMsgResp;
import com.feige.api.rpc.RpcClient;
import com.feige.api.session.Session;
import com.feige.fim.protocol.Packet;
import com.feige.framework.annotation.Inject;
import com.feige.utils.spi.annotation.SpiComp;

@SpiComp(value = "singleChat", interfaces = MsgHandler.class)
public class SingleChatMsgHandler extends AbstractMsgHandler{
    
    @Inject
    private RpcClient rpcClient;
    
    @Override
    public void handle(Session session, Packet msg) throws RemotingException {
        if (!session.isBindClient()) {
            this.sendErrorPacket(session, msg, ProtocolConst.ErrorCode.NOT_BIND, "NOT BIND");
            return;
        }
        ChatMsgResp chatMsgResp = rpcClient.sendMsg(msg);
        Packet ackPacket = buildAckPacket(msg, chatMsgResp);
        session.write(ackPacket);
    }
    
    
    private Packet buildAckPacket(Packet msg, ChatMsgResp chatMsgResp){
        return this.buildPacket(Command.ACK, SerializedClass.ACK, msg, (Ack ack) -> {
            ack.setServerMsgId(chatMsgResp.getServerMsgId());
            ack.setClientMsgId(chatMsgResp.getClientMsgId());
            ack.setSendTime(chatMsgResp.getSendTime());
            ack.setExtra(chatMsgResp.getExtra());
        });
    }

    @Override
    public byte getCmd() {
        return Command.SINGLE_CHAT.getCmd();
    }
}
