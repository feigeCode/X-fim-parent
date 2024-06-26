package com.feige.fim.bind;

import com.feige.api.constant.ClientType;

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
    private int clientType;

    /**
     * client version
     */
    private String clientVersion;

    /**
     *  client id
     */
    private String clientId;
    
    private String tags;

    /**
     * session id
     */
    private String sessionId;

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

    public int getClientType() {
        return clientType;
    }

    public ClientBindInfo setClientType(int clientType) {
        this.clientType = clientType;
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
    
    public String getTags(){
        return tags;
    }
    
    public ClientBindInfo setTags(String tags){
        this.tags = tags;
        return this;
    }

    public String getSessionId() {
        return sessionId;
    }

    public ClientBindInfo setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }
    
    public boolean isOnline() {
        return sessionId != null;
    }

    public boolean isOffline() {
        return sessionId == null;
    }

    public ClientBindInfo offline() {
        this.sessionId = null;
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
        return clientType == that.clientType && Objects.equals(this.clientId, that.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientType, clientId);
    }
}
