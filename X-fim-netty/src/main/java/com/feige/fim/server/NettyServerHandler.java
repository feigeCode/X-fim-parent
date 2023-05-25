package com.feige.fim.server;

import com.feige.api.handler.SessionHandler;
import com.feige.api.session.Session;
import com.feige.api.session.SessionRepository;
import com.feige.fim.adapter.NettySession;
import com.feige.fim.lg.Loggers;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author feige<br />
 * @ClassName: NettyServerHandler <br/>
 * @Description: <br/>
 * @date: 2023/5/21 14:07<br/>
 */
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private final SessionHandler sessionHandler;
    private final SessionRepository sessionRepository;
    private final static String HEARTBEAT_CNT = "hb_cnt";
    private final static int MAX_TIMEOUT_CNT = 3;

    public NettyServerHandler(SessionHandler sessionHandler, SessionRepository sessionRepository) {
        this.sessionHandler = sessionHandler;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        sessionHandler.received(toSession(ctx), msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        sessionHandler.connected(toSession(ctx));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        sessionHandler.disconnected(toSession(ctx));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        sessionHandler.caught(toSession(ctx), cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            final Session session = toSession(ctx);
            final IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                Integer hbCnt = (Integer) session.getAttr(HEARTBEAT_CNT);
                if (hbCnt == null){
                    hbCnt = 0;
                }else {
                    if (hbCnt >= MAX_TIMEOUT_CNT){
                        session.close();
                        Loggers.HEARTBEAT.warn("The session is closed because the client heartbeat is not received for a long time. Procedure, id = {}, remote address = {}", session.getId(), session.getRemoteAddress());
                        return;
                    }
                    hbCnt++;
                }
                session.setAttr(HEARTBEAT_CNT, hbCnt);
                Loggers.HEARTBEAT.debug("read idle, id = {}, remote address = {}, heartbeat count = {}", session.getId(), session.getRemoteAddress(), hbCnt);
            }
            if (idleStateEvent.state() == IdleState.WRITER_IDLE){
                if (!session.isBindClient()) {
                    session.close();
                    Loggers.HEARTBEAT.warn("Close the session of the unbound client, id = {}, remote address = {}", session.getId(), session.getRemoteAddress());
                }
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    protected Session toSession(ChannelHandlerContext ctx){
        return NettySession.getOrAddSession(ctx, sessionRepository);
    }
}
