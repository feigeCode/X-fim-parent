package com.feige.im.sender;

import com.feige.im.listener.MsgStatusListener;
import com.feige.im.utils.AssertUtil;
import com.feige.im.utils.NameThreadFactory;
import com.feige.im.utils.ScheduledThreadPoolExecutorUtil;
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

    private static final Map<String, WaitingAckTimer> WAITING_ACK_TIMER_CONTAINER = new ConcurrentHashMap<>();

    private static final ScheduledThreadPoolExecutorUtil EXECUTOR_UTIL = ScheduledThreadPoolExecutorUtil.getInstance();

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
        AssertUtil.notNull(statusListener,"statusListener");
        return statusListener;
    }

    public static void setStatusListener(MsgStatusListener statusListener) {
        AssertUtil.notNull(statusListener,"statusListener");
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
            this.timeout = TIMER.newTimeout(ignore -> EXECUTOR_UTIL.execute(() -> {
                // 超时未收到ack的消息
                statusListener.timeoutMsg(this.msgId);
            }), DURATION.toMillis(), TimeUnit.MILLISECONDS);
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
