package com.feige.api.sc;

import com.feige.api.base.Listener;
import com.feige.api.base.Service;


public interface IServer extends Service {

    /**
     *
     * @param listener
     */
    void bind(Listener listener);

}
