package com.feige.api.cache;

import java.io.Serializable;

/**
 * @author feige<br />
 * @ClassName: ObjectCache <br/>
 * @Description: <br/>
 * @date: 2023/5/25 22:19<br/>
 */
public interface ObjectCache<V extends Serializable> extends Cacheable {

    int set(V o);

    V get();
}