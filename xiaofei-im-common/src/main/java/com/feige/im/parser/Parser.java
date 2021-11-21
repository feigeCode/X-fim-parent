package com.feige.im.parser;

import com.feige.im.pojo.proto.DefaultMsg;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBufInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author feige<br />
 * @ClassName: ParsingMap <br/>
 * @Description: <br/>
 * @date: 2021/10/8 15:59<br/>
 */
public class Parser {

    @FunctionalInterface
    public interface DeserializationHandler{
        Message process(ByteBufInputStream in) throws Exception;
    }

    private static final Logger LOG =  LogManager.getLogger(Parser.class);

    public static Map<Integer,DeserializationHandler> map1 = new ConcurrentHashMap<>();
    public static Map<Class<? extends Message>,Integer> map2 = new ConcurrentHashMap<>();


    public static void add(Integer key, Class<? extends Message> t,DeserializationHandler handler){
        if (!Objects.isNull(key) && (key.equals(1) || key.equals(0))){
            LOG.error("0和1已经被分配为心跳key，请重新分配key！");
            throw new IllegalArgumentException("0和1已经被分配为心跳key，请重新分配key！");
        }
        if (!Objects.isNull(key) && !Objects.isNull(t) && !Objects.isNull(handler)){
            if (map1.containsKey(key)){
                LOG.error("{}该key已被其它解析器占用，请重新分配key！",key);
                throw new IllegalArgumentException("该key已被其它解析器占用，请重新分配key！");
            }
            map1.put(key,handler);
            map2.put(t,key);
            LOG.info("key = {}, className = {}的解析器已加入map中",key,t.getCanonicalName());
        }
    }

    public static <T> T getT(Class<T> clazz,Message msg){
        return clazz.cast(msg);
    }

    public static Message getMessage(Integer key, ByteBufInputStream in){
        DeserializationHandler handler = map1.get(key);
        if (Objects.nonNull(handler)){
            try {
                return handler.process(in);
            } catch (Exception e) {
                LOG.error(e.getMessage(),e);
            }
        }
        LOG.error("未找到key = {}的解析器，请查看客户端发送的数据类型标识是否和当前解析器匹配",key);
        return null;
    }

    public static Integer getKey(Class<? extends Message> t){
        return map2.get(t);
    }

    public static void registerDefaultParsing(){
        add(2, DefaultMsg.Auth.class, DefaultMsg.Auth::parseFrom);
        add(3,DefaultMsg.Msg.class, DefaultMsg.Msg::parseFrom);
        add(4, DefaultMsg.Forced.class, DefaultMsg.Forced::parseFrom);
        // 集群才注册
        add(5,DefaultMsg.ClusterAuth.class,DefaultMsg.ClusterAuth::parseFrom);
    }
}
