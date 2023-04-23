package com.feige.api.handler;

import com.feige.api.session.ISession;

import java.net.InetSocketAddress;

public class RemotingException extends Exception {

    private static final long serialVersionUID = -3160452149606778709L;

    private final InetSocketAddress localAddress;

    private final InetSocketAddress remoteAddress;

    public RemotingException(ISession session, String msg) {
        this(session == null ? null : session.getLocalAddress(), session == null ? null : session.getRemoteAddress(),
                msg);
    }

    public RemotingException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, String message) {
        super(message);

        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }

    public RemotingException(ISession session, Throwable cause) {
        this(session == null ? null : session.getLocalAddress(), session == null ? null : session.getRemoteAddress(),
                cause);
    }

    public RemotingException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, Throwable cause) {
        super(cause);

        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }

    public RemotingException(ISession session, String message, Throwable cause) {
        this(session == null ? null : session.getLocalAddress(), session == null ? null : session.getRemoteAddress(),
                message, cause);
    }

    public RemotingException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, String message,
                             Throwable cause) {
        super(message, cause);

        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }

    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }
}