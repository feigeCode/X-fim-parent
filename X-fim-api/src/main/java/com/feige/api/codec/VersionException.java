package com.feige.api.codec;

public class VersionException extends DecoderException{

    private static final long serialVersionUID = 3546272712208105199L;

    /**
     * Creates a new instance.
     */
    public VersionException() {
    }

    /**
     * Creates a new instance.
     */
    public VersionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance.
     */
    public VersionException(String message) {
        super(message);
    }

    /**
     * Creates a new instance.
     */
    public VersionException(Throwable cause) {
        super(cause);
    }
}
