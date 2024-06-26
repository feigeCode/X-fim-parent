package com.feige.api.session;

import com.feige.api.handler.RemotingException;
import com.feige.api.sc.Listener;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

public interface SessionRepository {

    /**
     * get by session id
     * @param id  session id
     * @return session
     */
    Session get(String id);

    /**
     * add session
     * @param session session
     */
    void add(Session session);

    /**
     * add if absent session
     * @param id id
     * @param mappingFunction session mapping function
     * @return
     */
    default Session computeIfAbsent(String id, Function<String, Session> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        Session session;
        if ((session = get(id)) == null) {
            Session newSession;
            if ((newSession = mappingFunction.apply(id)) != null) {
                add(newSession);
                return newSession;
            }
        }
        return session;
    }

    /**
     * remove and close
     * @param session session
     */
    void removeAndClose(Session session);


    /**
     * send msg
     * @param id session id
     * @param msg message
     * @throws RemotingException
     */
    default void write(String id, Object msg) throws RemotingException {
        write(id, msg, null);
    }
    
    void write(String id, Object msg, Listener listener) throws RemotingException;

    /**
     * Whether to include session
     * @param id session id
     * @return
     */
    boolean contains(String id);

    Collection<Session> getAll();
}
