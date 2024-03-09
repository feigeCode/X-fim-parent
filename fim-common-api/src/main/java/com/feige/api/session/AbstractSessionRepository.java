package com.feige.api.session;

import com.feige.api.handler.RemotingException;
import com.feige.api.sc.Listener;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractSessionRepository implements SessionRepository {
    protected final Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    
    @Override
    public Session get(String sid){
        return sessionMap.get(sid);
    }

    @Override
    public void add(Session session) {
        String id = session.getId();
        sessionMap.put(id, session);

    }

    @Override
    public void removeAndClose(Session session) {
        sessionMap.remove(session.getId());
        if(!session.isClosed()){
            session.close();
        }
    }
    

    @Override
    public void write(String id, Object msg, Listener listener) throws RemotingException {
        Session session = get(id);
        if (session == null){
            return;
        }
        session.write(msg, listener);
    }

    @Override
    public boolean contains(String id) {
        if (id != null && !id.isEmpty()){
            return sessionMap.containsKey(id);
        }
        return false;
    }

    @Override
    public Collection<Session> getAll() {
        return Collections.unmodifiableCollection(sessionMap.values());
    }
}
