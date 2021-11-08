package com.feige.im.pojo;

/**
 * @author feige<br />
 * @ClassName: Server <br/>
 * @Description: <br/>
 * @date: 2021/11/7 23:01<br/>
 */
public class Server {

    private final String ip;
    private final int port;

    public Server(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return ip + ":" + port;
    }
}
