package com.feige.im;

import com.feige.im.client.ImClient;

/**
 * @author feige<br />
 * @ClassName: Main <br/>
 * @Description: <br/>
 * @date: 2021/11/6 23:19<br/>
 */
public class ClientMain {

    public static void main(String[] args) {
        ImClient.connect("localhost",8090,(key, channel, msg) -> {
            System.out.println(msg);
        });
    }
}
