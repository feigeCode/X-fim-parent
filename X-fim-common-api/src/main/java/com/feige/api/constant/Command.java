package com.feige.api.constant;

public enum Command {
    HEARTBEAT(1),
    HANDSHAKE(2),
    BIND(3),
    FAST_CONNECT(4),
    SINGLE_CHAT(5),
    GROUP_CHAT(6),
    ACK(7),
    NACK(8),
    PAUSE(9),
    RESUME(10),
    ERROR(11),
    OK(12),
    KICK(13),
    PUSH(14),
    NOTIFICATION(15),
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
