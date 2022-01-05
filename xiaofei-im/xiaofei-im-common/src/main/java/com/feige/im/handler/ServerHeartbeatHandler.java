package com.feige.im.handler;

import com.feige.im.constant.ChannelAttr;
import com.feige.im.constant.ImConst;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.utils.StringUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;

import java.util.Objects;

/**
 * @author feige<br />
 * @ClassName: ServerHeartbeatHandler <br/>
 * @Description: <br/>
 * @date: 2021/10/6 21:38<br/>
 */
@ChannelHandler.Sharable
public class ServerHeartbeatHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger();

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String userId = ctx.channel().attr(ChannelAttr.USER_ID).get();
            String nodeKey = ctx.channel().attr(ChannelAttr.NODE_KEY).get();
            if (event.state() == IdleState.WRITER_IDLE ){
                if (StringUtil.isEmpty(userId) && StringUtil.isEmpty(nodeKey)){
                    ctx.channel().close();
                    LOG.warn("{} 关闭未绑定ID的连接.",ctx.channel().id().asShortText());
                    return;
                }
                // 发送心跳
                Attribute<Integer> attr = ctx.channel().attr(ChannelAttr.PING_COUNT);
                if (Objects.isNull(attr.get())){
                    attr.set(1);
                }else {
                    attr.set(attr.get() + 1);
                }
                if (!StringUtil.isEmpty(userId)){
                    LOG.debugInfo("发送心跳，userId={}", userId);
                }

                if (!StringUtil.isEmpty(nodeKey)){
                    LOG.debugInfo("发送心跳，nodeKey={}", nodeKey);
                }
                ctx.channel().writeAndFlush(ImConst.PING_MSG);
                return;
            }
            Integer pingCount = ctx.channel().attr(ChannelAttr.PING_COUNT).get();
            if (event.state() == IdleState.READER_IDLE && !Objects.isNull(pingCount) && pingCount >= ImConst.PONG_TIME_OUT_COUNT) {
                ctx.close();
                if (!StringUtil.isEmpty(userId)){
                    LOG.warn("userId = {} pong timeout.",userId);
                }

                if (!StringUtil.isEmpty(nodeKey)){
                    LOG.warn("nodeKey = {} pong timeout.",nodeKey);
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
