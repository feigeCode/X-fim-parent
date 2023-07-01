package com.feige.fim.protocol;

public enum Command {
    HEARTBEAT(1),
    HANDSHAKE(2),
    LOGIN(3),
    LOGOUT(4),
    BIND(5),
    UNBIND(6),
    FAST_CONNECT(7),
    PAUSE(8),
    RESUME(9),
    ERROR(10),
    OK(11),
    KICK(12),
    PUSH(13),
    NOTIFICATION(14),
    CHAT(15),
    GROUP(16),
    ACK(17),
    NACK(18),
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
