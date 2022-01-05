package com.feige.im.group;


import com.feige.im.constant.ChannelAttr;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.utils.StringUtil;
import com.google.protobuf.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.Attribute;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author feige<br />
 * @ClassName: MyChannelGroup <br/>
 * @Description: <br/>
 * @date: 2021/10/7 14:49<br/>
 */
public class MyChannelGroup {

    private static final Logger LOG = LoggerFactory.getLogger();

    // value为集合是因为考虑到同一个用户登录了多个平台 ios | android | web | pc | mac
    private static final Map<String,Collection<Channel>> MAP = new ConcurrentHashMap<>();
    private static final Collection<Channel> EMPTY_LIST = new ConcurrentLinkedQueue<>();
    // 监听连接关闭
    private transient final ChannelFutureListener remover = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            future.removeListener(this);
            remove(future.channel());
        }
    };

    private MyChannelGroup(){

    }

    private static final MyChannelGroup INSTANCE = new MyChannelGroup();

    public static MyChannelGroup getInstance() {
        return INSTANCE;
    }

    /**
     * @description: 获取用户的唯一标识
     * @author: feige
     * @date: 2021/10/9 21:10
     * @param	channel	当前用户的通道
     * @return: java.lang.String
     */
    protected String getKey(Channel channel){
        Attribute<String> attr = channel.attr(ChannelAttr.USER_ID);
        if (!Objects.isNull(attr)){
            return attr.get();
        }
        return null;
    }

    /**
     * @description: 把channel保存到MAP中
     * @author: feige
     * @date: 2021/10/9 22:08
     * @param	channel
     * @return: void
     */
    public void add(Channel channel){
        String key = getKey(channel);
        if (StringUtil.isEmpty(key) || !channel.isActive()){
            return;
        }
        // 绑定移除事件
        channel.closeFuture().addListener(remover);
        Collection<Channel> channels = MAP.getOrDefault(key, new ConcurrentLinkedQueue<>());
        // 为空说明是第一个连接
        if (channels.isEmpty()){
            MAP.put(key, channels);
        }
        LOG.debugInfo("userId = {}的连接加入到group中，上线平台为：{}",key,channel.attr(ChannelAttr.PLATFORM).get());
        channels.add(channel);

        if (!channel.isActive()){
            LOG.warn("userId = {}的连接被移除",key);
            remove(channel);
        }
    }

    /**
     * @description: 移除channel
     * @author: feige
     * @date: 2021/10/9 22:09
     * @param	channel
     * @return: void
     */
    public void remove(Channel channel){
        String key = getKey(channel);
        if (StringUtil.isEmpty(key)){
            return;
        }
        Collection<Channel> collection = MAP.getOrDefault(key, EMPTY_LIST);
        collection.remove(channel);
        if (collection.isEmpty()){
            MAP.remove(key);
        }
    }

    /**
     * @description: 通过用户的唯一标识获取对应平台的channel
     * @author: feige
     * @date: 2021/10/9 22:09
     * @param	key	用户唯一标识
     * @param	platform 平台标识
     * @return: java.util.Collection<io.netty.channel.Channel>
     */
    public Collection<Channel> getChannels(String key,String... platform){
        List<String> platforms = Arrays.asList(platform);
        return getChannels(key)
                .stream()
                .filter(channel -> platforms.contains(channel.attr(ChannelAttr.PLATFORM).get()))
                .collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
    }

    /**
     * @description: 获取用户的channel
     * @author: feige
     * @date: 2021/10/9 22:10
     * @param	key
     * @return: java.util.Collection<io.netty.channel.Channel>
     */
    public Collection<Channel> getChannels(String key){
        return MAP.getOrDefault(key,EMPTY_LIST);
    }

    /**
     * @description: 往对应key的通道中写数据
     * @author: feige
     * @date: 2021/10/9 22:11
     * @param	key
     * @param	msg
     * @return: void
     */
    public void write(String key, Message msg){
        getChannels(key).forEach(channel -> channel.writeAndFlush(msg));
    }

    /**
     * @description: 通过用户唯一标识往符合断言的通道写数据
     * @author: feige
     * @date: 2021/10/9 22:12
     * @param	key 唯一标识
     * @param	msg 消息
     * @param	predicate 断言规则，用来筛选满足条件的channel
     * @return: void
     */
    public void write(String key, Message msg, Predicate<Channel> predicate){
        getChannels(key).stream().filter(predicate).forEach(channel -> channel.writeAndFlush(msg));
    }







}
