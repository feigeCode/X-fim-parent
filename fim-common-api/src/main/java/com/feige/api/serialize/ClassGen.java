package com.feige.api.serialize;

import com.feige.api.msg.Msg;

import java.util.Arrays;

public class ClassGen {
    private Class<?> msgClass;
    
    private Object[] args;

    public ClassGen(Class<?> msgClass, Object... args) {
        this.msgClass = msgClass;
        this.args = args;
    }

    public <T extends Msg> Class<T> getMsgClass() {
        return (Class<T>) msgClass;
    }

    public void setMsgClass(Class<?> msgClass) {
        this.msgClass = msgClass;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "ClassGen{" +
                "msgClass=" + msgClass +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
