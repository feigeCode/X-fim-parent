package com.feige.api.constant;

public enum ClientType {
    WINDOWS(0),
    MAC(1),
    LINUX(2),
    ANDROID(3),
    IOS(4),
    APPLET(5),
    H5(6);

    private final int code;

    ClientType(int code){
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }

    public static ClientType valueOf(int code){
        ClientType[] values = values();
        for (ClientType value : values) {
            if (value.getCode() == code){
                return value;
            }
        }
        throw new IllegalArgumentException("unknown type code=" + code);
    }
    
}
