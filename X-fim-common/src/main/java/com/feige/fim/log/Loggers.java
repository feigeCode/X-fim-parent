package com.feige.fim.log;

import com.feige.api.log.Logger;

public interface Loggers {

    Logger SRD = LoggerFactoryLoad.getLogger("srd-log","srd.log");
    Logger TASK = LoggerFactoryLoad.getLogger("task-log","task.log");
    Logger SERVER = LoggerFactoryLoad.getLogger("server-log","server.log");

}
