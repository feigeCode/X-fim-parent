package com.feige.api.constant;

public enum Command {
    HEARTBEAT(1),
    HANDSHAKE(2),
    BIND(3),
    FAST_CONNECT(4),
    PAUSE(5),
    RESUME(6),
    ERROR(7),
    OK(8),
    KICK(9),
    PUSH(10),
    NOTIFICATION(11),
    CHAT(12),
    GROUP(13),
    ACK(14),
    NACK(15),
    UNKNOWN(-1);

    Command(int cmd) {
        this.cmd = (byte) cmd;
    }

    private final byte cmd;

    public byte getCmd(){
        return this.cmd;
    }

    public static Command valueOf(byte cmd) {
        Command[] values = values();
        for (Command value : values) {
            if (value.cmd == cmd){
                return value;
            }
        }
        throw new IllegalArgumentException("未知类型cmd=" + cmd);
    }
}
