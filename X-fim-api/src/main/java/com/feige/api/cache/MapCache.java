package com.feige.api.cache;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * @author feige<br />
 * @ClassName: MapCache <br/>
 * @Description: <br/>
 * @date: 2023/5/25 22:16<br/>
 */
public interface MapCache<K extends Serializable, V extends Serializable> extends Cacheable{

    V put(K key, V value);

    void putAll(Map<? extends K, ? extends V> map);

    void clearAndPutAll(Map<? extends K, ? extends V> map);

    void loadAll(boolean replaceExistingValues);

    V get(K key);

    boolean containsKey(K key);

    /**
     * @param keys
     * @return
     */
    Map<K, V> getAll(Set<K> keys);

    void forEach(BiConsumer<? super K, ? super V> action);

    V remove(K key);
}
