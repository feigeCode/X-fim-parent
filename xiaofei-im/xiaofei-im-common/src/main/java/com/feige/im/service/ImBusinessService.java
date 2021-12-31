package com.feige.im.service;

import com.google.protobuf.Message;

import java.util.List;

/**
 * @author feige<br />
 * @ClassName: ImBusinessService <br/>
 * @Description: <br/>
 * @date: 2021/12/31 9:39<br/>
 */
public interface ImBusinessService {

    /**
     * 持久化消息
     * @param message
     * @return
     */
    long persistentMsg(Message message);

    /**
     * 通过群组ID获取群成员
     * @param GroupId
     * @return
     */
    List<String> getUserIdsByGroupId(String GroupId);


}
