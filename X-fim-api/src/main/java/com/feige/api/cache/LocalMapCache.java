package com.feige.api.cache;

import java.io.Serializable;

/**
 * @author feige<br />
 * @ClassName: LocalMapCache <br/>
 * @Description: <br/>
 * @date: 2023/5/25 22:17<br/>
 */
public interface LocalMapCache<K extends Serializable, V extends Serializable>  extends MapCache<K, V>{
    V putIfAbsent(K key, V value);

    V getOrDefault(K key, V defaultValue);
}
