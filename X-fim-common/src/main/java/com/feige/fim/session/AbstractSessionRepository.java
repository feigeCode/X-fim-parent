package com.feige.fim.session;

import com.feige.api.handler.RemotingException;
import com.feige.api.session.Session;
import com.feige.api.session.SessionRepository;
import com.feige.fim.utils.StringUtil;

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
        session.close();
    }
    

    @Override
    public void write(String id, Object msg) throws RemotingException {
        Session session = get(id);
        session.write(msg);
    }

    @Override
    public boolean contains(String id) {
        if (StringUtil.isNotBlank(id)){
            return sessionMap.containsKey(id);
        }
        return false;
    }
}
