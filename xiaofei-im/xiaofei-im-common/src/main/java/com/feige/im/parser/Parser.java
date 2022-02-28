package com.feige.im.parser;

import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.pojo.proto.Ack;
import com.feige.im.pojo.proto.DefaultMsg;
import com.feige.im.pojo.proto.HeartBeat;
import com.feige.im.service.ImBusinessService;
import com.google.protobuf.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        /**
         * 消息转换
         * @param bytes 字节数组
         * @return
         * @throws Exception
         */
        Message process(byte[] bytes) throws Exception;
    }


    @FunctionalInterface
    public interface ReceiverIdsHandler{

        /**
         * 通过消息获取接收者ID，返回一个列表考虑了群聊
         * @param msg 接收到的消息
         * @param imBusinessService 通过群ID获取群成员列表
         * @return 返回接受者IDS
         * @throws Exception
         */
        List<String> apply(Message msg, ImBusinessService imBusinessService) throws Exception;
    }

    private static final Logger LOG =  LoggerFactory.getLogger();

    public static final Map<Integer,DeserializationHandler> DESERIALIZATION_MAP = new ConcurrentHashMap<>();
    public static final Map<Class<? extends Message>,Integer> MSG_KEY_MAP = new ConcurrentHashMap<>();
    public static final Map<Class<? extends Message>, ReceiverIdsHandler> RECEIVER_IDS_MAP = new ConcurrentHashMap<>();


    static {
        // ack消息
        add(999, Ack.AckMsg.class,Ack.AckMsg::parseFrom, (msg, imBusinessService) -> {
            Ack.AckMsg ackMsg = getT(Ack.AckMsg.class, msg);
            List<String> receiverIds = new ArrayList<>();
            receiverIds.add(ackMsg.getReceiverId());
            return receiverIds;
        });
    }


    public static void add(Integer key, Class<? extends Message> t,DeserializationHandler handler, ReceiverIdsHandler receiverIds){
        add(key, t, handler);
        RECEIVER_IDS_MAP.put(t,receiverIds);
    }

    public static void add(Integer key, Class<? extends Message> t,DeserializationHandler handler){
        if (Integer.valueOf(1).equals(key) || Integer.valueOf(0).equals(key)){
            LOG.error("0和1已经被分配为心跳key，请重新分配key！");
            throw new IllegalArgumentException("0和1已经被分配为心跳key，请重新分配key！");
        }
        if (!Objects.isNull(key) && !Objects.isNull(t) && !Objects.isNull(handler)){
            if (DESERIALIZATION_MAP.containsKey(key)){
                LOG.error("{}该key已被其它解析器占用，请重新分配key！",key);
                throw new IllegalArgumentException("该key已被其它解析器占用，请重新分配key！");
            }
            DESERIALIZATION_MAP.put(key,handler);
            MSG_KEY_MAP.put(t,key);
            LOG.info("key = {}, className = {}的解析器已加入map中",key,t.getCanonicalName());
        }
    }

    public static <T> T getT(Class<T> clazz,Message msg){
        return clazz.cast(msg);
    }

    /**
     * 获取消息接收者ID，通过接收者ID推送消息
     * @param message 消息
     * @param imBusinessService 通过群ID获取群成员列表
     * @return
     */
    public static List<String> getReceiverIds(Message message, ImBusinessService imBusinessService){
        if (message instanceof HeartBeat.Pong || message instanceof HeartBeat.Ping){
            return Collections.emptyList();
        }
        Class<? extends Message> messageClass = message.getClass();
        ReceiverIdsHandler receiverIdsHandler = RECEIVER_IDS_MAP.get(messageClass);
        if (Objects.isNull(receiverIdsHandler)){
            LOG.error("未发现消息类型为{}的消息有取消息的函数，请检查是否添加了该函数", messageClass.getCanonicalName());
            return Collections.emptyList();
        }
        try {
            return receiverIdsHandler.apply(message, imBusinessService);
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }
        return Collections.emptyList();
    }

    public static Message getMessage(Integer key, byte[] bytes){
        DeserializationHandler handler = DESERIALIZATION_MAP.get(key);
        if (Objects.nonNull(handler)){
            try {
                return handler.process(bytes);
            } catch (Exception e) {
                LOG.error(e.getMessage(),e);
            }
        }
        LOG.error("未找到key = {}的解析器，请查看客户端发送的数据类型标识是否和当前解析器匹配",key);
        return null;
    }

    public static Integer getKey(Class<? extends Message> t){
        return MSG_KEY_MAP.get(t);
    }

    public static void registerDefaultParsing(){
        add(2, DefaultMsg.Auth.class, DefaultMsg.Auth::parseFrom);
        add(3, DefaultMsg.Forced.class, DefaultMsg.Forced::parseFrom);
        add(4,DefaultMsg.TransportMsg.class,DefaultMsg.TransportMsg::parseFrom, (msg, imBusinessService) -> {
            DefaultMsg.TransportMsg transportMsg = getT(DefaultMsg.TransportMsg.class, msg);
            DefaultMsg.Msg msgMsg = transportMsg.getMsg();
            if (Objects.isNull(msgMsg)) {
                return Collections.emptyList();
            }
            List<String> receiverIds = new ArrayList<>();
            DefaultMsg.TransportMsg.MsgType type = transportMsg.getType();
            String receiverId = msgMsg.getReceiverId();
            if (type == DefaultMsg.TransportMsg.MsgType.PRIVATE){
                receiverIds.add(receiverId);
            }else if (type == DefaultMsg.TransportMsg.MsgType.GROUP){
                // 群成员ID
                return imBusinessService.getUserIdsByGroupId(receiverId);
            }
            return receiverIds;
        });
    }
}
