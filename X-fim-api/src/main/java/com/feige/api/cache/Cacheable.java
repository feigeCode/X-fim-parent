package com.feige.api.cache;

/**
 * @author feige<br />
 * @ClassName: Cacheable <br/>
 * @Description: <br/>
 * @date: 2023/5/25 22:15<br/>
 */
public interface Cacheable {
    
    String getName();

    int size();

    boolean isEmpty();

    void clear();
}
