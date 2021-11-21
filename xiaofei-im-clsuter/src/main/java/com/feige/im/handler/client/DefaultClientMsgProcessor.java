package com.feige.im.handler.client;

import com.feige.im.constant.ProcessorEnum;
import com.feige.im.handler.MsgProcessor;
import com.feige.im.parser.Parser;
import com.feige.im.pojo.proto.DefaultMsg;
import com.feige.im.utils.StringUtil;
import com.google.protobuf.Message;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author feige<br />
 * @ClassName: DefaultClientMsgProcessor <br/>
 * @Description: <br/>
 * @date: 2021/11/14 17:19<br/>
 */
public class DefaultClientMsgProcessor extends ClusterClientMsgProcessor {


    @Override
    public String getReceiverId(Message message) {
        if (message instanceof DefaultMsg.Msg){
            DefaultMsg.Msg msg = Parser.getT(DefaultMsg.Msg.class, message);
            return msg.getReceiverId();
        }
        return null;
    }
}
