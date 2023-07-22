package com.feige.api.session;

import com.feige.api.cipher.Cipher;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author feige<br />
 * @ClassName: AbstractSession <br/>
 * @Description: <br/>
 * @date: 2023/5/21 16:45<br/>
 */
public abstract class AbstractSession implements Session {
    protected final AtomicBoolean active = new AtomicBoolean(false);
    protected final AtomicBoolean bindClient = new AtomicBoolean(false);
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
        return bindClient.get();
    }

    @Override
    public void setBindClient(boolean isBindClient) {
        bindClient.set(isBindClient);
    }
}