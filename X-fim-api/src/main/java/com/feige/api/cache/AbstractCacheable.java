package com.feige.api.cache;


/**
 * @author feige<br />
 * @ClassName: AbstractCacheable <br/>
 * @Description: <br/>
 * @date: 2023/5/25 22:19<br/>
 */
public abstract class AbstractCacheable implements Cacheable {
    private final String group;
    private final String name;
    private final String fullName;

    public AbstractCacheable(String group, String name) {
        this.group = group;
        this.name = name;
        this.fullName = group + ":" + name;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

}