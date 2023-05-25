package com.feige.api.cache;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * @author feige<br />
 * @ClassName: SetCache <br/>
 * @Description: <br/>
 * @date: 2023/5/25 22:19<br/>
 */
public interface SetCache<V extends Serializable> extends Cacheable {

    boolean contains(V o);

    Iterator<V> iterator();

    Set<V> toSet();

    int add(V o);

    int remove(V o);

    int addAll(Collection<? extends V> c);

    int reload(Collection<? extends V> set);

    int removeAll(Collection<? extends V> c);
}
