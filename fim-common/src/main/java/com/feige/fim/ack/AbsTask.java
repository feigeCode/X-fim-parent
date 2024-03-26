package com.feige.fim.ack;

import com.feige.api.session.Session;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Future;

@Getter
@Setter
public abstract class AbsTask<T> implements Runnable {

    protected T packet;
    protected AckCallback<T> callback;
    protected int retryCnt = 3;
    protected int delay = 1000;
    protected long sendTime = System.currentTimeMillis();
    protected Future<?> future;
    protected AckManager ackManager;
    protected Session session;


    public AbsTask(T packet, AckCallback<T> callback, Session session) {
        this.packet = packet;
        this.callback = callback;
        this.session = session;
    }

    @Override
    public void run() {
        ackManager.getAndRemove(this.getId());
        if (this.retryCnt > 0) {
            try {
                this.retryCnt--;
                AbsTask<T> task = this.copy();
                ackManager.addTask(task);
                session.write(packet);
            } catch (Throwable cause) {
                if (callback != null) {
                    callback.onFailure(packet, cause);
                }
            }
        } else {
            if (callback != null) {
                callback.onTimeout(packet);
            }
        }
    }

    public void ack(T response){
        if (this.future.cancel(true)) {
            if (callback != null) {
                callback.onSuccess(response);
            }
        }
    }
    public abstract AbsTask<T> copy();

    public abstract int getId();

}
