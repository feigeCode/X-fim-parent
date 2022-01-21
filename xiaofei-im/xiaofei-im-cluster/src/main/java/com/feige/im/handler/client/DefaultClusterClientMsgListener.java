package com.feige.im.handler.client;

import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.DefaultMsg;
import com.feige.im.service.ImBusinessService;
import com.google.protobuf.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * @author feige<br />
 * @ClassName: DefaultClientMsgProcessor <br/>
 * @Description: <br/>
 * @date: 2021/11/14 17:19<br/>
 */
public class DefaultClusterClientMsgListener extends ClusterClientMsgListener {

    private final ImBusinessService imBusinessService;

    public DefaultClusterClientMsgListener(ImBusinessService imBusinessService) {
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
        return receiverIds;
    }
}
