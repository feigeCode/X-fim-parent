package com.feige.fim.session;

import com.feige.api.session.Session;
import com.feige.api.session.SessionRepository;
import com.feige.fim.utils.StringUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractSessionRepository implements SessionRepository {
    
    protected final Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    protected final Map<String, List<SessionInfo>> sessionInfosMap = new ConcurrentHashMap<>();

    /**
     * @description: 获取唯一标识
     * @author: feige
     * @date: 2021/10/9 21:10
     * @param	session	当前用户的通道
     * @return: java.lang.String
     */
    protected String getSid(Session session) {
        return session.getId();
    }

    @Override
    public void add(Session session) {
        String sid = getSid(session);
        
    }

    @Override
    public void remove(Session session) {

    }

    @Override
    public Collection<Session> getSessions(String uid) {
        return null;
    }

    @Override
    public void write(String uid, Object msg) {

    }

    @Override
    public boolean containsSession(String uid, Integer clientType) {
        if (StringUtil.isNotBlank(uid)){
            List<SessionInfo> sessionInfos = sessionInfosMap.get(uid);
            return true;
        }
        return false;
    }

    @Override
    public String getKey() {
        return null;
    }
}
