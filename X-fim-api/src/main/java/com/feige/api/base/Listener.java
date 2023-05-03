package com.feige.api.base;

public interface Listener {

    void onSuccess(Object... args);

    void onFailure(Throwable cause);
}
