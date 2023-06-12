package com.feige.fim.codec;

public class CheckSumException extends DecoderException{

    private static final long serialVersionUID = 3546272712208105199L;

    /**
     * Creates a new instance.
     */
    public CheckSumException() {
    }

    /**
     * Creates a new instance.
     */
    public CheckSumException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance.
     */
    public CheckSumException(String message) {
        super(message);
    }

    /**
     * Creates a new instance.
     */
    public CheckSumException(Throwable cause) {
        super(cause);
    }
}
