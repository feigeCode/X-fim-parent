package com.feige.im.handler.server;

import com.feige.im.handler.MsgListener;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.Ack;
import com.feige.im.pojo.proto.DefaultMsg;
import com.feige.im.service.ImBusinessService;
import com.google.protobuf.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * @author feige<br />
 * @ClassName: DefaultClusterMsgForwardListener <br/>
 * @Description: <br/>
 * @date: 2022/1/21 14:14<br/>
 */
public class DefaultClusterMsgForwardListener extends ClusterMsgForwardListener{
    private final ImBusinessService imBusinessService;

    public DefaultClusterMsgForwardListener(MsgListener msgListener, ImBusinessService imBusinessService) {
        super(msgListener);
        this.imBusinessService = imBusinessService;
    }

    @Override
    public List<String> getReceiverIds(Message message) {
        List<String> receiverIds = new ArrayList<>();
        if (message instanceof DefaultMsg.TransportMsg){
            DefaultMsg.TransportMsg transportMsg = Parser.getT(DefaultMsg.TransportMsg.class, message);
            DefaultMsg.Msg msg = transportMsg.getMsg();
            DefaultMsg.TransportMsg.MsgType type = transportMsg.getType();
            String receiverId = msg.getReceiverId();
            if (type == DefaultMsg.TransportMsg.MsgType.PRIVATE){
                receiverIds.add(receiverId);
            }else if (type == DefaultMsg.TransportMsg.MsgType.GROUP){
                // 群成员ID
                return imBusinessService.getUserIdsByGroupId(receiverId);
            }
        }

        if (message instanceof Ack.AckMsg){
            Ack.AckMsg ackMsg = Parser.getT(Ack.AckMsg.class, message);
            receiverIds.add(ackMsg.getReceiverId());
        }
        return receiverIds;
    }
}
