package com.feige.api.handler;

/**
 * @author feige<br />
 * @ClassName: AbstractMsgHandler <br/>
 * @Description: <br/>
 * @date: 2023/5/25 21:50<br/>
 */
public abstract class AbstractMsgHandler implements MsgHandler {

    @Override
    public String getKey() {
        return String.valueOf(getCmd());
    }

    /**
     * cmd
     * @return cmd
     */
    public abstract int getCmd();
}
