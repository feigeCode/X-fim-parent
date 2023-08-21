package com.feige.api.session;

import com.feige.api.handler.RemotingException;
import com.feige.api.sc.Listener;
import com.feige.utils.common.StringUtils;

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
        session.write(msg, listener);
    }

    @Override
    public boolean contains(String id) {
        if (StringUtils.isNotBlank(id)){
            return sessionMap.containsKey(id);
        }
        return false;
    }
}
