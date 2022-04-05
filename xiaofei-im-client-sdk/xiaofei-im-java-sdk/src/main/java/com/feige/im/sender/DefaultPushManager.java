package com.feige.im.sender;

import com.feige.im.listener.MsgStatusListener;
import com.feige.im.pojo.proto.DefaultMsg;

/**
 * @author feige<br />
 * @ClassName: DefaultPushManager <br/>
 * @Description: <br/>
 * @date: 2022/2/26 14:32<br/>
 */
public class DefaultPushManager extends PushManager{


    public static void pushMsg(DefaultMsg.TransportMsg transportMsg){
        String msgId = transportMsg.getMsg().getId();
        MsgStatusListener statusListener = WaitingAckTimerHandler.getStatusListener();
        if (push(transportMsg)) {
            // 消息发送成功，创建等待ack定时器
            WaitingAckTimerHandler.add(WaitingAckTimerHandler.getWaitingAckTimer(msgId));
            statusListener.hasMsgSent(msgId);
        }else {
            statusListener.exceptionMsg(msgId);
        }
    }
}
