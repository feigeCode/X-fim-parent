package com.feige.im.service.impl;

import com.feige.im.service.ImBusinessService;
import com.google.protobuf.Message;

import java.util.Collections;
import java.util.List;

/**
 * @author feige<br />
 * @ClassName: ImBusinessServiceImpl <br/>
 * @Description: <br/>
 * @date: 2021/12/31 14:23<br/>
 */
public class ImBusinessServiceImpl implements ImBusinessService {
    @Override
    public long persistentMsg(Message message) {
        return 0;
    }

    @Override
    public List<String> getUserIdsByGroupId(String GroupId) {
        return Collections.emptyList();
    }
}
