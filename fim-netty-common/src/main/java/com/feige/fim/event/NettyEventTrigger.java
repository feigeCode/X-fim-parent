package com.feige.fim.event;

import com.feige.api.session.Session;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NettyEventTrigger {
    private Session session;
    private Object event;
}
