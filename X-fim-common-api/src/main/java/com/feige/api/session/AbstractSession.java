package com.feige.api.session;

import com.feige.api.crypto.Cipher;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author feige<br />
 * @ClassName: AbstractSession <br/>
 * @Description: <br/>
 * @date: 2023/5/21 16:45<br/>
 */
public abstract class AbstractSession implements Session {
    protected final AtomicBoolean active = new AtomicBoolean(false);
    protected final AtomicInteger sessionState = new AtomicInteger(0);
    protected Cipher cipher;

    private final Map<String, Object> attributes = new ConcurrentHashMap<>();


    public void markActive(boolean isActive) {
        active.set(isActive);
    }
    
    @Override
    public boolean hasAttr(String key) {
        return attributes.containsKey(key);
    }

    @Override
    public Object getAttr(String key) {
        return attributes.get(key);
    }

    @Override
    public void setAttr(String key, Object value) {
        attributes.put(key, value);
    }

    @Override
    public void removeAttr(String key) {
        attributes.remove(key);
    }

    @Override
    public Cipher getCipher() {
        return cipher;
    }

    @Override
    public void setCipher(Cipher cipher) {
        this.cipher = cipher;
    }

    @Override
    public void close() {
        this.markActive(false);
        this.attributes.clear();
    }

    @Override
    public boolean isClosed() {
        return !active.get();
    }

    @Override
    public boolean isConnected() {
        return active.get();
    }

    @Override
    public boolean isBindClient() {
        return sessionState.get() >= 2;
    }

    @Override
    public void markBindClient() {
        sessionState.set(2);
    }

    @Override
    public boolean isHandshake() {
        return sessionState.get() >= 1;
    }

    @Override
    public void markHandshake() {
        sessionState.set(1);
    }
    
}
