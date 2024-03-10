package com.feige.fim.ack;

public interface AckCallback<T> {
    void onSuccess(T packet);

    void onTimeout(T packet);

    void onFailure(T packet, Throwable cause);
}