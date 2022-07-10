package com.feige.im.parser;

import com.feige.im.constant.ImConst;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.pojo.proto.Ack;
import com.feige.im.pojo.proto.DefaultMsg;
import com.feige.im.pojo.proto.HeartBeat;
import com.feige.im.service.ImBusinessService;
import com.feige.im.utils.StringUtil;
import com.google.protobuf.Message;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

import java.nio.charset.StandardCharsets;
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
    public interface WsAuthMsgConverter {
        /**
         * ws授权消息转换
         * @param handshakeComplete
         * @return
         */
        Message convert(WebSocketServerProtocolHandler.HandshakeComplete handshakeComplete);
    }


    @FunctionalInterface
    public interface DeserializationHandler {
        /**
         * 消息转换
         * @param bytes 字节数组
         * @return
         * @throws Exception
         */
        Message process(byte[] bytes) throws Exception;
    }


    @FunctionalInterface
    public interface ReceiverIdsHandler {

        /**
         * 通过消息获取接收者ID，返回一个列表考虑了群聊
         * @param msg 接收到的消息
         * @param imBusinessService 通过群ID获取群成员列表
         * @return 返回接受者IDS
         * @throws Exception
         */
        List<String> apply(Object msg, ImBusinessService imBusinessService) throws Exception;
    }

    private static final Logger LOG =  LoggerFactory.getLogger();

    protected static final Map<Integer,DeserializationHandler> DESERIALIZATION_MAP = new ConcurrentHashMap<>();
    protected static final Map<Class<?>,ParserKey> MSG_KEY_MAP = new ConcurrentHashMap<>();
    protected static final Map<Class<?>, ReceiverIdsHandler> RECEIVER_IDS_MAP = new ConcurrentHashMap<>();
    public static WsAuthMsgConverter WS_AUTH_MSG_CONVERTER = (handshakeComplete) -> {
        try {
            HttpHeaders headers = handshakeComplete.requestHeaders();
            String token = headers.get("token");
            String address;
            String osVersion;
            String deviceName;
            String language;
            String deviceId;
            String ip;
            String version;
            String platform;
            if (StringUtil.isBlank(token)){
                String uri = handshakeComplete.requestUri();
                QueryStringDecoder queryString = new QueryStringDecoder(uri, StandardCharsets.UTF_8);
                Map<String, List<String>> parameters = queryString.parameters();
                address = parameters.get("address").get(0);
                osVersion = parameters.get("osVersion").get(0);
                deviceName = parameters.get("deviceName").get(0);
                language = parameters.get("language").get(0);
                deviceId = parameters.get("deviceId").get(0);
                ip = parameters.get("ip").get(0);
                version = parameters.get("version").get(0);
                platform = parameters.get("platform").get(0);
                token = parameters.get("token").get(0);
            }else {
                token = headers.get("token");
                address = headers.get("address");
                osVersion = headers.get("osVersion");
                deviceName = headers.get("deviceName");
                language = headers.get("language");
                deviceId = headers.get("deviceId");
                ip = headers.get("ip");
                version = headers.get("version");
                platform = headers.get("platform");
            }
            return DefaultMsg.Auth.newBuilder()
                    .setAddress(address)
                    .setDeviceName(deviceName)
                    .setLanguage(language)
                    .setOsVersion(osVersion)
                    .setDeviceId(deviceId)
                    .setIp(ip)
                    .setVersion(version)
                    .setPlatform(platform)
                    .setToken(token)
                    .build();
        }catch (Exception e){
            LOG.error("authenticate error:",e);
        }
        return null;
    };

    static {
        // ack消息
        add(new ParserKey(999, 1), Ack.AckMsg.class,Ack.AckMsg::parseFrom, (msg, imBusinessService) -> {
            Ack.AckMsg ackMsg = getT(Ack.AckMsg.class, msg);
            List<String> receiverIds = new ArrayList<>();
            receiverIds.add(ackMsg.getReceiverId());
            return receiverIds;
        });
    }


    public static void add(ParserKey key, Class<? extends Message> t,DeserializationHandler handler, ReceiverIdsHandler receiverIds){
        add(key, t, handler);
        RECEIVER_IDS_MAP.put(t,receiverIds);
    }

    public static void add(ParserKey key, Class<?> t,DeserializationHandler handler){
        DeserializationHandler deserializationHandler = DESERIALIZATION_MAP.get(key);
        if (deserializationHandler != null){
            LOG.error(key + "已被其它解析器占用，请重新分配key！");
            return;
        }
        if (Objects.nonNull(key) && Objects.nonNull(t) && Objects.nonNull(handler)){
            DESERIALIZATION_MAP.put(key.getUid(),handler);
            MSG_KEY_MAP.put(t,key);
            LOG.info("key = {}, className = {}的解析器已加入map中",key,t.getCanonicalName());
        }else {
            LOG.warn("所有参数不能为空，请检查传递的参数！");
        }
    }

    public static <T> T getT(Class<T> clazz, Object msg){
        boolean instance = clazz.isInstance(msg);
        if (instance){
            return clazz.cast(msg);
        }else {
            return null;
        }
    }

    /**
     * 获取消息接收者ID，通过接收者ID推送消息
     * @param message 消息
     * @param imBusinessService 通过群ID获取群成员列表
     * @return
     */
    public static List<String> getReceiverIds(Object message, ImBusinessService imBusinessService){
        if (message instanceof HeartBeat.Pong || message instanceof HeartBeat.Ping){
            return Collections.emptyList();
        }
        Class<?> messageClass = message.getClass();
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

    /**
     * 设置ws授权消息转换器
     * @param wsAuthMsgConverter
     */
    public static void setWsAuthMsgConvert(WsAuthMsgConverter wsAuthMsgConverter){
        if (wsAuthMsgConverter != null){
            WS_AUTH_MSG_CONVERTER = wsAuthMsgConverter;
        }
    }

    public static ParserKey getKey(Object msg){
        if (msg instanceof HeartBeat.Pong){
            return new ParserKey(ImConst.PONG_MSG_TYPE, -1);
        } else if (msg instanceof HeartBeat.Ping){
            return new ParserKey(ImConst.PING_MSG_TYPE, -1);
        }else {
            return MSG_KEY_MAP.get(msg.getClass());
        }
    }

    public static void registerDefaultParsing(){
        add(new ParserKey(2, 1), DefaultMsg.Auth.class, DefaultMsg.Auth::parseFrom);
        add(new ParserKey(3, 1), DefaultMsg.Forced.class, DefaultMsg.Forced::parseFrom);
        add(new ParserKey(4, 1),DefaultMsg.TransportMsg.class,DefaultMsg.TransportMsg::parseFrom, (msg, imBusinessService) -> {
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

    public static class ParserKey {
        /**
         * 类唯一标识
         */
        private int uid;

        /**
         * 所用解析器类型
         */
        private int parserType;

        public ParserKey() {

        }

        public ParserKey(int uid, int parserType) {
            this.uid = uid;
            this.parserType = parserType;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getParserType() {
            return parserType;
        }

        public void setParserType(int parserType) {
            this.parserType = parserType;
        }

        @Override
        public String toString() {
            return "ParserKey{" +
                    "uid=" + uid +
                    ", parserType=" + parserType +
                    '}';
        }
    }
}

