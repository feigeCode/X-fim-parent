package com.feige.api.handler;


import com.feige.framework.annotation.Inject;

import com.feige.api.serialize.SerializedClassManager;

/**
 * @author feige<br />
 * @ClassName: AbstractMsgHandler <br/>
 * @Description: <br/>
 * @date: 2023/5/25 21:50<br/>
 */
public abstract class AbstractMsgHandler<T> implements MsgHandler<T> {
    

    
    @Inject
    protected SerializedClassManager serializedClassManager;
    
    
  
}
