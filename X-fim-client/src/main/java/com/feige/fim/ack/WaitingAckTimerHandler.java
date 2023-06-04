package com.feige.fim.ack;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WaitingAckTimerHandler {

    private static final HashedWheelTimer TIMER = new HashedWheelTimer(Executors.defaultThreadFactory());

    private static final Map<String, WaitingAckTimer> WAITING_ACK_TIMER_CONTAINER = new ConcurrentHashMap<>();


    public static final Duration DURATION = Duration.ofSeconds(5);

    private static MsgStatusListener statusListener;

    public static void add(WaitingAckTimer waitingAckTimer){
        WAITING_ACK_TIMER_CONTAINER.put(waitingAckTimer.getMsgId(), waitingAckTimer);
    }

    public static void remove(String msgId){
        WaitingAckTimer waitingAckTimer = WAITING_ACK_TIMER_CONTAINER.remove(msgId);
        waitingAckTimer.cancel();
        // 消息已送达
        statusListener.hasMsgArrived(msgId);
    }

    public static MsgStatusListener getStatusListener() {
        assert statusListener != null;
        return statusListener;
    }

    public static void setStatusListener(MsgStatusListener statusListener) {
        assert statusListener != null;
        WaitingAckTimerHandler.statusListener = statusListener;
    }

    public static WaitingAckTimer getWaitingAckTimer(String msgId){
        return new WaitingAckTimer(msgId);
    }

    public static class WaitingAckTimer {

        private String msgId;

        private Timeout timeout;

        public WaitingAckTimer(String msgId) {
            this.msgId = msgId;
            createTimeout();
        }

        public void createTimeout(){
            this.timeout = TIMER.newTimeout(ignore -> {
                // 超时未收到ack的消息
                statusListener.timeoutMsg(this.msgId);
            }, DURATION.toMillis(), TimeUnit.MILLISECONDS);
        }


        public void cancel(){
            if (this.timeout == null || this.timeout.isCancelled() || this.timeout.isExpired()) {
                return;
            }
            this.timeout.cancel();
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public Timeout getTimeout() {
            return timeout;
        }

        public void setTimeout(Timeout timeout) {
            this.timeout = timeout;
        }
    }
}