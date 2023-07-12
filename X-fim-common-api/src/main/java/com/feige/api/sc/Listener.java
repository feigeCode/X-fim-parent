package com.feige.api.sc;

public interface Listener {

    void onSuccess(Object... args);

    void onFailure(Throwable cause);
}
