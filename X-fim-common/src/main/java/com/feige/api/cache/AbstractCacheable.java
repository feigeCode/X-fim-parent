package com.feige.api.cache;


/**
 * @author feige<br />
 * @ClassName: AbstractCacheable <br/>
 * @Description: <br/>
 * @date: 2023/5/25 22:19<br/>
 */
public abstract class AbstractCacheable implements Cacheable {
    private final String name;

    public AbstractCacheable(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }

 

}
