package com.feige.fim.push;

import com.feige.fim.api.PushService;

/**
 * @author feige<br />
 * @ClassName: PushManager <br/>
 * @Description: <br/>
 * @date: 2023/6/4 16:04<br/>
 */
public class PushManager {
    private PushService pushService;
    
    
    public static boolean push(Object msg) {
        return getInstance().getPushService().push(msg);
    }

    public PushService getPushService() {
        return pushService;
    }

    public void setPushService(PushService pushService) {
        this.pushService = pushService;
    }
    
    public static PushManager getInstance(){
        return PushManagerInner.PUSH_MANAGER;
    }
    
    static class PushManagerInner {
        private static final PushManager PUSH_MANAGER = new PushManager();
    }
}
