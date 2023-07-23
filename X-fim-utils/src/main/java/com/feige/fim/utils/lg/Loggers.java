package com.feige.fim.utils.lg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Loggers {
    Logger CONSOLE = LoggerFactory.getLogger("console");
    Logger SRD = LoggerFactory.getLogger("srd.log");
    Logger TASK = LoggerFactory.getLogger("task.log");
    Logger SERVER = LoggerFactory.getLogger("server.log");
    Logger CODEC = LoggerFactory.getLogger("codec.log");
    Logger LOADER = LoggerFactory.getLogger("loader.log");
    Logger UTILS = LoggerFactory.getLogger("utils.log");
    Logger HEARTBEAT = LoggerFactory.getLogger("heartbeat.log");

}
