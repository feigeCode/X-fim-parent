package com.feige.api.sc;

public interface Listener {

    void onSuccess(Object... args);

    void onFailure(Throwable cause);
    
    class CallbackListener implements Listener {
        
        private final Callback<Object[]> successCallback;
        private Callback<Throwable> failureCallback;

        public CallbackListener(Callback<Object[]> successCallback, Callback<Throwable> failureCallback) {
            this.successCallback = successCallback;
            this.failureCallback = failureCallback;
        }

        public CallbackListener(Callback<Object[]> successCallback) {
            this.successCallback = successCallback;
        }

        @Override
        public void onSuccess(Object... args) {
            if (successCallback != null){
                successCallback.call(args);
            }
        }

        @Override
        public void onFailure(Throwable cause) {
            if (failureCallback != null){
                failureCallback.call(cause);
            }
        }
    }
}
