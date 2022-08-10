package com.feige.log;

public interface Loggers {

    Logger SRD = LoggerFactoryLoad.getLogger("srd-log","srd.log");
    Logger TASK = LoggerFactoryLoad.getLogger("task-log","task.log");
    Logger SERVER = LoggerFactoryLoad.getLogger("server-log","server.log");
    Logger CODEC = LoggerFactoryLoad.getLogger("codec-log","codec.log");

}
