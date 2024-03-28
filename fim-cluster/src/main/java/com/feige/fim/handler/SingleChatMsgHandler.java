package com.feige.fim.handler;

import com.feige.api.constant.Command;
import com.feige.api.constant.ProtocolConst;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.session.Session;
import com.feige.fim.protocol.Packet;
import com.feige.fim.rpc.RpcTransporter;
import lombok.Setter;
import com.feige.utils.spi.annotation.SPI;

@SPI(value = "singleChat", interfaces = MsgHandler.class)
public class SingleChatMsgHandler extends AbstractMsgHandler {
    
    @Setter
    private RpcTransporter<Packet> rpcTransporter;
    
    @Override
    public void handle(Session session, Packet msg) throws RemotingException {
        if (!session.isBindClient()) {
            this.sendErrorPacket(session, msg, ProtocolConst.ErrorCode.NOT_BIND, "NOT BIND");
            return;
        }
        Packet respPacket = rpcTransporter.rpcClient().exchange(msg);
        session.write(respPacket);
    }

    @Override
    public byte getCmd() {
        return Command.SINGLE_CHAT.getCmd();
    }
}
