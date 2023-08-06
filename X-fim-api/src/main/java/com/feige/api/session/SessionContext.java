package com.feige.api.session;

import java.io.Serializable;

public class SessionContext implements Serializable {
    private String sessionKey;
    private String iv;
    private String clientVersion;
    private String clientId;
    private String osName;
    private String osVersion;
    private int clientType;

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public int getClientType() {
        return clientType;
    }

    public void setClientType(int clientType) {
        this.clientType = clientType;
    }

    @Override
    public String toString() {
        return "SessionContext{" +
                "sessionKey='" + sessionKey + '\'' +
                ", iv='" + iv + '\'' +
                ", clientVersion='" + clientVersion + '\'' +
                ", clientId='" + clientId + '\'' +
                ", osName='" + osName + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", clientType=" + clientType +
                '}';
    }
}
