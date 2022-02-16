package com.feige.im;

import com.feige.im.client.ImClient;
import com.feige.im.handler.MsgListener;
import com.feige.im.sender.PushManager;

/**
 * @author feige<br />
 * @ClassName: ClientStarter <br/>
 * @Description: <br/>
 * @date: 2022/2/5 20:47<br/>
 */
public class ClientStarter {

    public static void start(String ip, int port, MsgListener listener){
        ImClient client = ImClient.connect(ip, port, listener);
        PushManager.setChannel(client.getChannel());
    }
}
