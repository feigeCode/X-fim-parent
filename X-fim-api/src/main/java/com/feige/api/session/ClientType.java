package com.feige.api.session;

import com.feige.api.constant.Const;

import java.util.ArrayList;
import java.util.List;

public enum ClientType {
    WINDOWS(0, Const.PC),
    MAC(1, Const.PC),
    LINUX(2, Const.PC),
    ANDROID(3, Const.MOBILE),
    IOS(4, Const.MOBILE),
    APPLET(5, Const.MOBILE),
    H5(6, Const.WEB);

    private final int code;
    private final int terminalType;

    ClientType(int code, int terminalType){
        this.code = code;
        this.terminalType = terminalType;
    }

    public int getTerminalType(){
        return this.terminalType;
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

    public static List<ClientType> getByTerminalType(int terminalType){
        ArrayList<ClientType> clientTypes = new ArrayList<>();
        ClientType[] values = values();
        for (ClientType value : values) {
            if (value.getTerminalType() == terminalType){
                clientTypes.add(value);
            }
        }
        return clientTypes;
    }
}
