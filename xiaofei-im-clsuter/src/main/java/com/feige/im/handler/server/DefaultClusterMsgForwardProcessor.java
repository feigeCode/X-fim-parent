package com.feige.im.handler.server;

import com.feige.im.handler.MsgProcessor;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.DefaultMsg;
import com.google.protobuf.Message;

/**
 * @author feige<br />
 * @ClassName: DefaultClusterMsgForwardProcessor <br/>
 * @Description: <br/>
 * @date: 2021/11/14 16:33<br/>
 */
public class DefaultClusterMsgForwardProcessor extends ClusterMsgForwardProcessor{
    public DefaultClusterMsgForwardProcessor(MsgProcessor processor) {
        super(processor);
    }

    @Override
    public String getReceiverId(Message message) {
        if (message instanceof DefaultMsg.Msg){
            DefaultMsg.Msg msg = Parser.getT(DefaultMsg.Msg.class, message);
            return msg.getReceiverId();
        }
        return null;
    }
}
