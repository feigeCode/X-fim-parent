package com.feige.fim.config;


import com.feige.fim.utils.StringUtils;
import org.bouncycastle.util.encoders.Base64;

import java.util.Arrays;
import java.util.List;

public class ClientConfig {

    private String sessionId;
    private long expireTime;
    private byte[] clientKey;
    private byte[] iv;
    private String clientVersion;
    private String clientId;
    private String osName;
    private String osVersion;
    private int osCode;
    private String token;

    public String serializeString() {
        String[] strings = {
                String.valueOf(this.expireTime),
                this.sessionId,
                Base64.toBase64String(this.clientKey),
                Base64.toBase64String(this.iv),
                this.token
        };
        return StringUtils.commaJoiner.join(strings);
    }

    public boolean isExpired() {
        return expireTime < System.currentTimeMillis();
    }

    public void deserializeString(String str) {
        List<String> list = StringUtils.commaSplitter.splitToList(str);
        if (list.size() == 5){
            this.expireTime = Long.parseLong(list.get(0));
            if (isExpired()){
                return;
            }
            this.sessionId = list.get(1);
            this.clientKey = Base64.decode(list.get(2));
            this.iv = Base64.decode(list.get(3));
            this.token = list.get(4);
        }
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public byte[] getClientKey() {
        return clientKey;
    }

    public void setClientKey(byte[] clientKey) {
        this.clientKey = clientKey;
    }

    public byte[] getIv() {
        return iv;
    }

    public void setIv(byte[] iv) {
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

    public int getOsCode() {
        return osCode;
    }

    public void setOsCode(int osCode) {
        this.osCode = osCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "ClientConfig{" +
                "sessionId='" + sessionId + '\'' +
                ", expireTime=" + expireTime +
                ", clientKey=" + Arrays.toString(clientKey) +
                ", iv=" + Arrays.toString(iv) +
                ", clientVersion='" + clientVersion + '\'' +
                ", clientId='" + clientId + '\'' +
                ", osName='" + osName + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", osCode=" + osCode +
                ", token='" + token + '\'' +
                '}';
    }
}