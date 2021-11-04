package com.feige.im.handler;

import com.feige.im.constant.ProcessorEnum;
import com.google.protobuf.Message;
import io.netty.channel.Channel;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author feige<br />
 * @ClassName: ProtocolProcessor <br/>
 * @Description: <br/>
 * @date: 2021/10/9 10:16<br/>
 */
public interface ProtocolProcessor {

    void process(ProcessorEnum key, Channel channel, Message msg);


}
