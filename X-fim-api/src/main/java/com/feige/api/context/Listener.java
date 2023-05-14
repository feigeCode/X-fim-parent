package com.feige.api.context;

public interface Listener {

    void onSuccess(Object... args);

    void onFailure(Throwable cause);
}
