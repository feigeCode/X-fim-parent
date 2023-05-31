package com.feige.api.bind;

import java.io.Serializable;
import java.util.Objects;

public class ClientBindInfo implements Serializable {
    

    /**
     * ip
     */
    private String host;

    /**
     * port
     */
    private int port;

    /**
     * client os code
     * @see ClientType
     */
    private int osCode;

    /**
     * client version
     */
    private String clientVersion;

    /**
     *  client id
     */
    private String clientId;

    /**
     * session id
     */
    private String sid;

    public String getHost() {
        return host;
    }

    public ClientBindInfo setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public ClientBindInfo setPort(int port) {
        this.port = port;
        return this;
    }

    public int getOsCode() {
        return osCode;
    }

    public ClientBindInfo setOsCode(int osCode) {
        this.osCode = osCode;
        return this;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public ClientBindInfo setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public ClientBindInfo setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getSid() {
        return sid;
    }

    public ClientBindInfo setSid(String sid) {
        this.sid = sid;
        return this;
    }
    
    public ClientType getClientType() {
        return ClientType.valueOf(this.osCode);
    }
    
    public boolean isOnline() {
        return sid != null;
    }

    public boolean isOffline() {
        return sid == null;
    }

    public ClientBindInfo offline() {
        this.sid = null;
        return this;
    }

    public boolean isThisMachine(String host, int port) {
        return this.port == port && this.host.equals(host);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientBindInfo that = (ClientBindInfo) o;
        return osCode == that.osCode && Objects.equals(this.clientId, that.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(osCode, clientId);
    }
}
