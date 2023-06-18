package com.feige.fim.netty;

import com.feige.fim.event.ChannelActive;
import com.feige.fim.protocol.Command;
import com.feige.fim.protocol.Packet;
import com.feige.fim.utils.EventDispatcher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartbeatHandler extends ChannelInboundHandlerAdapter {
    private final NettyClient nettyClient;

    public HeartbeatHandler(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE){
                EventDispatcher.fire(new ChannelActive(nettyClient, ChannelActive.CHANNEL_INACTIVE));
            }
            
            if (state == IdleState.WRITER_IDLE){
                if (heartbeatTask == null){
                    heartbeatTask = new HeartbeatTask(ctx);
                }
                nettyClient.getGroup().execute(heartbeatTask);
            }

        }
        super.userEventTriggered(ctx, evt);
    }


    private HeartbeatTask heartbeatTask;
    private static class HeartbeatTask implements Runnable {

        private final ChannelHandlerContext ctx;

        public HeartbeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            Channel channel = ctx.channel();
            if (channel.isActive()) {
                Packet packet = Packet.create(Command.HEARTBEAT);
                channel.writeAndFlush(packet);
            }
        }
    }
}
