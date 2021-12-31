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
public class DefaultClientMsgProcessor extends ClusterClientMsgProcessor {

    private ImBusinessService imBusinessService;

    public DefaultClientMsgProcessor(ImBusinessService imBusinessService) {
        this.imBusinessService = imBusinessService;
    }

    @Override
    public List<String> getReceiverIds(Message message) {
        List<String> receiverIds = new ArrayList<>();
        if (message instanceof DefaultMsg.TransportMsg){
            DefaultMsg.TransportMsg transportMsg = Parser.getT(DefaultMsg.TransportMsg.class, message);
            DefaultMsg.Msg msg = transportMsg.getMsg();
            int type = transportMsg.getType();
            String receiverId = msg.getReceiverId();
            if (type == 1){
                receiverIds.add(receiverId);
            }else if (type == 2){
                // 群成员ID
                return imBusinessService.getUserIdsByGroupId(receiverId);
            }
        }
        return receiverIds;
    }
}
