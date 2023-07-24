package com.feige.fim.serialize;

import com.feige.annotation.SpiComp;
import com.feige.api.serialize.AbstractSerializedClassManager;
import com.feige.api.serialize.SerializedClassManager;
import com.google.auto.service.AutoService;

@SpiComp
@AutoService(SerializedClassManager.class)
public class MsgSerializedClassManager extends AbstractSerializedClassManager {
    
}
