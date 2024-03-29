package com.feige.fim.handler;

import com.feige.api.constant.Command;
import com.feige.api.handler.MsgHandler;
import com.feige.api.handler.RemotingException;
import com.feige.api.session.Session;
import com.feige.fim.constant.ServerConfigKey;
import com.feige.fim.protocol.Packet;
import com.feige.fim.rpc.RpcTransporter;
import com.feige.framework.annotation.ConditionOnConfig;
import com.feige.utils.spi.annotation.SPI;
import lombok.Setter;

@Setter
@SPI(value = "singleChat", interfaces = MsgHandler.class)
@ConditionOnConfig(key = ServerConfigKey.RUNNING_MODE, value = "cluster")
public class SingleChatMsgHandler extends AbstractMsgHandler {
    
    private RpcTransporter<Packet> rpcTransporter;
    
    @Override
    public void handle(Session session, Packet msg) throws RemotingException {
        if (!this.checkBind(session, msg)) {
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
