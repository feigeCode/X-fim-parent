package com.feige.im.receiver;

import com.feige.im.pojo.proto.Ack;
import com.feige.im.sender.PushManager;
import com.feige.im.utils.ScheduledThreadPoolExecutorUtil;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author feige<br />
 * @ClassName: MsgHandler <br/>
 * @Description: <br/>
 * @date: 2022/2/5 21:18<br/>
 */
public class MsgHandler {

    private static final ScheduledThreadPoolExecutorUtil EXECUTOR_UTIL = ScheduledThreadPoolExecutorUtil.getInstance();
    /**
     * 判断消息是否重复发送
     * @param msgId 消息ID
     * @return
     */
    public static boolean isRepeat(String msgId){
        return false;
    }


    /**
     * 发送ack
     * @param ctx channel上下文
     * @param ackMsg ack消息
     */
    public static void sendAck(Ack.AckMsg ackMsg){
        EXECUTOR_UTIL.execute(() -> PushManager.push(ackMsg));
    }


}
