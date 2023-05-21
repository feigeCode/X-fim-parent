package com.feige.fim.session;

import com.feige.api.session.ISession;
import com.feige.api.session.SessionRepository;
import com.feige.fim.utils.StringUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractSessionRepository implements SessionRepository {
    
    private final Map<String, List<ISession>> sessionsMap = new ConcurrentHashMap<>();
    
    @Override
    public String getUid(ISession session) {
        return session.getId();
    }

    @Override
    public void add(ISession session) {
        String uid = getUid(session);
        
    }

    @Override
    public void remove(ISession session) {

    }

    @Override
    public Collection<ISession> getSessions(String uid) {
        return null;
    }

    @Override
    public void write(String uid, Object msg) {

    }

    @Override
    public boolean containsSession(String uid, Integer clientType) {
        if (StringUtil.isNotBlank(uid)){
            List<ISession> sessions = sessionsMap.get(uid);
            return true;
        }
        return false;
    }

    @Override
    public String getKey() {
        return null;
    }
}
