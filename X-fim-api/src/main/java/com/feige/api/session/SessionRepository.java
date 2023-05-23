package com.feige.api.session;

import com.feige.api.spi.Spi;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

public interface SessionRepository extends Spi {



    /**
     * @description: 把session保存到MAP中
     * @author: feige
     * @date: 2021/10/9 22:08
     * @param	session
     * @return: void
     */
    void add(Session session);

    /**
     * @description: 移除session
     * @author: feige
     * @date: 2021/10/9 22:09
     * @param	session
     * @return: void
     */
    void remove(Session session);

    /**
     * @description: 通过唯一标识获取对应平台的session
     * @author: feige
     * @date: 2021/10/9 22:09
     * @param	uid	唯一标识
     * @param	platform 平台标识
     * @return: java.util.Collection<io.netty.session.Session>
     */
    default Collection<Session> getSessions(String uid, int... platform) {
        return Collections.emptyList();
    };

    /**
     * @description: 通过唯一标识获取会话
     * @author: feige
     * @date: 2021/10/9 22:10
     * @param	uid
     * @return: java.util.Collection<io.netty.session.Session>
     */
    Collection<Session> getSessions(String uid);

    /**
     * @description: 往对应uid的通道中写数据
     * @author: feige
     * @date: 2021/10/9 22:11
     * @param	uid
     * @param	msg
     * @return: void
     */
    void write(String uid, Object msg);

    /**
     * @description: 通过唯一标识往符合断言的通道写数据
     * @author: feige
     * @date: 2021/10/9 22:12
     * @param	uid 唯一标识
     * @param	msg 消息
     * @param	predicate 断言规则，用来筛选满足条件的session
     * @return: void
     */
    default void write(String uid, Object msg, Predicate<Session> predicate){

    }

    /**
     * Whether to include session
     * @param uid USER ID
     * @param clientType client type
     * @return
     */
    boolean containsSession(String uid, Integer clientType);
}
