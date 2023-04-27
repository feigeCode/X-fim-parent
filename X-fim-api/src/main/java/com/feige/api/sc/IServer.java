package com.feige.api.sc;

import com.feige.api.base.Service;
import com.feige.api.handler.SessionHandler;


public interface IServer extends Service {

    /**
     *bind
     * @param sessionHandler
     */
    void bind(SessionHandler sessionHandler);

}
