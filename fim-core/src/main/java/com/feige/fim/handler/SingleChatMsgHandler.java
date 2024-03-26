package com.feige.fim.handler;

import com.feige.api.constant.Command;
import com.feige.api.handler.MsgHandler;
import com.feige.api.msg.ChatMsgReq;
import com.feige.fim.protocol.Packet;
import com.feige.utils.spi.annotation.SPI;

@SPI(value = "singleChat", interfaces = MsgHandler.class)
public class SingleChatMsgHandler extends ForwardMsgHandler {

    @Override
    public String getReceiverId(Packet packet) {
        ChatMsgReq chatMsgReq = this.getMsg(packet, ChatMsgReq.class);
        return chatMsgReq.getReceiverId();
    }

    @Override
    public byte getCmd() {
        return Command.SINGLE_CHAT.getCmd();
    }
}
