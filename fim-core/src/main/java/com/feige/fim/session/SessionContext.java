package com.feige.fim.session;

import com.feige.utils.common.StringUtils;

import java.util.List;

public class SessionContext {
    private String clientId;
    private int clientType;
    private String clientVersion;
    private String osName;
    private String osVersion;
    private long expireTime;
    private String[] cipherArgs;
    

    public String getClientId() {
        return this.clientId;
    }

    public SessionContext setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public int getClientType() {
        return this.clientType;
    }

    public SessionContext setClientType(int clientType) {
        this.clientType = clientType;
        return this;
    }

    public String getClientVersion() {
        return this.clientVersion;
    }

    public SessionContext setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
        return this;
    }

    public String getOsName() {
        return this.osName;
    }

    public SessionContext setOsName(String osName) {
        this.osName = osName;
        return this;
    }

    public String getOsVersion() {
        return this.osVersion;
    }

    public SessionContext setOsVersion(String osVersion) {
        this.osVersion = osVersion;
        return this;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public SessionContext setExpireTime(long expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    public String[] getCipherArgs() {
        return cipherArgs;
    }

    public SessionContext setCipherArgs(String[] cipherArgs) {
        this.cipherArgs = cipherArgs;
        return this;
    }

    public String serializeString(){
        String[] args = getCipherArgs();
        int argLength = 0;
        if (args != null){
            argLength = args.length;
        }
        String[] sessionContext = new String[6 + argLength];
        sessionContext[0] = this.getOsName();
        sessionContext[1] = this.getOsVersion();
        sessionContext[2] = this.getClientVersion();
        sessionContext[3] = this.getClientId();
        sessionContext[4] = String.valueOf(this.getClientType());
        sessionContext[5] = String.valueOf(this.getExpireTime());
        if (args != null){
            System.arraycopy(args, 0, sessionContext, 6, argLength);
        }
        return StringUtils.commaJoiner.join(sessionContext);
    }
    
    
    public SessionContext deserialize(String sessionContextString){
        List<String> list = StringUtils.originCommaSplitter.splitToList(sessionContextString);
        this.setOsName(list.get(0))
                .setOsVersion(list.get(1))
                .setClientVersion(list.get(2))
                .setClientId(list.get(3))
                .setClientType(Integer.parseInt(list.get(4)))
                .setExpireTime(Long.parseLong(list.get(5)));
        if (list.size() > 6){
            String[] args = list.subList(6, list.size()).toArray(new String[list.size() - 6]);
            setCipherArgs(args);
        }
        return this;
    }
}
