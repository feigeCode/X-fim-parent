package com.feige.im.sender;

import com.feige.im.utils.NameThreadFactory;
import com.feige.im.utils.ScheduledThreadPoolExecutorUtil;
import com.google.protobuf.Message;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author feige<br />
 * @ClassName: WaitingAckTimerHandler <br/>
 * @Description: 等待ACK定时器handler<br/>
 * @date: 2022/2/6 22:29<br/>
 */
public class WaitingAckTimerHandler {

    private static final HashedWheelTimer TIMER = new HashedWheelTimer(new NameThreadFactory("waiting-ack-timer-"));

    private static final Map<Long, WaitingAckTimer> WAITING_ACK_TIMER_CONTAINER = new ConcurrentHashMap<>();

    private static final ScheduledThreadPoolExecutorUtil EXECUTOR_UTIL = ScheduledThreadPoolExecutorUtil.getInstance();

    public static final Duration DURATION = Duration.ofSeconds(5);

    public static void add(WaitingAckTimer waitingAckTimer){
        WAITING_ACK_TIMER_CONTAINER.putIfAbsent(waitingAckTimer.msgId, waitingAckTimer);
    }

    public static WaitingAckTimer remove(Long msgId){
        WaitingAckTimer waitingAckTimer = WAITING_ACK_TIMER_CONTAINER.remove(msgId);
        waitingAckTimer.cancel();
        return waitingAckTimer;
    }


    public static class WaitingAckTimer {

        private Long msgId;

        private Message message;

        private Timeout timeout;

        public WaitingAckTimer(Long msgId, Message message) {
            this.msgId = msgId;
            this.message = message;
            createTimeout();
        }

        public void createTimeout(){
            this.timeout = TIMER.newTimeout(ignore -> EXECUTOR_UTIL.execute(() -> {
                boolean isSuccess = PushManager.pushMsg(message);
                if (isSuccess){
                    WaitingAckTimer waitingAckTimer = remove(this.msgId);
                    add(new WaitingAckTimer(waitingAckTimer.msgId,waitingAckTimer.message));
                }
            }), DURATION.toMillis(), TimeUnit.MILLISECONDS);
        }


        public void cancel(){
            if (this.timeout == null || this.timeout.isCancelled() || this.timeout.isExpired()) {
                return;
            }
            this.timeout.cancel();
        }

        public Long getMsgId() {
            return msgId;
        }

        public void setMsgId(Long msgId) {
            this.msgId = msgId;
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }

        public Timeout getTimeout() {
            return timeout;
        }

        public void setTimeout(Timeout timeout) {
            this.timeout = timeout;
        }
    }
}
