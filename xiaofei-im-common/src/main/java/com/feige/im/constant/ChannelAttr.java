package com.feige.im.constant;


import io.netty.util.AttributeKey;

/**
 * @author feige<br />
 * @ClassName: ChannelAttr <br/>
 * @Description: <br/>
 * @date: 2021/10/6 21:44<br/>
 */
public interface ChannelAttr {

    /**
     * channel的short ID
     */
    AttributeKey<String> ID = AttributeKey.valueOf("id");
    /**
     * 用户的ID
     */
    AttributeKey<String> USER_ID = AttributeKey.valueOf("user_id");
    /**
     * 终端类型
     */
    AttributeKey<String> PLATFORM = AttributeKey.valueOf("platform");

    /**
     * 记录ping的次数
     */
    AttributeKey<Integer> PING_COUNT = AttributeKey.valueOf("ping_count");


    /**
     * 记录设备ID
     */
    AttributeKey<String> DEVICE_ID = AttributeKey.valueOf("device_id");


    /**
     * 记录设备语言
     */
    AttributeKey<String> LANGUAGE = AttributeKey.valueOf("language");


    /**
     * 节点的key
     */
    AttributeKey<String> NODE_KEY = AttributeKey.valueOf("node_key");


}
