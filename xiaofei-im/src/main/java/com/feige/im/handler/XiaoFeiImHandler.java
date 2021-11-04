package com.feige.im.handler;

import com.feige.im.constant.ChannelAttr;
import com.feige.im.constant.ImConst;
import com.feige.im.constant.ProcessorEnum;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author feige<br />
 * @ClassName: XiaoFeiImHandler <br/>
 * @Description: <br/>
 * @date: 2021/10/7 14:46<br/>
 */
@ChannelHandler.Sharable
public class XiaoFeiImHandler extends SimpleChannelInboundHandler<Message> {

    private final ProtocolProcessor processor;

    public XiaoFeiImHandler(ProtocolProcessor protocolProcessor){
        this.processor = protocolProcessor;
    }

    private static final Logger log = LogManager.getLogger(XiaoFeiImHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        processor.process(ProcessorEnum.READ,ctx.channel(),msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接进入");
        ctx.channel().attr(ChannelAttr.ID).set(ctx.channel().id().asShortText());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("userId = {}的连接不活跃",ctx.channel().attr(ChannelAttr.USER_ID));
        if (ctx.channel().attr(ChannelAttr.USER_ID) == null){
            return;
        }
        processor.process(ProcessorEnum.INACTIVE,ctx.channel(), null);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause);
    }
}
