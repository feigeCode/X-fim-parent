package com.feige.utils.spi;

public enum SpiScope {
    /**
     * 全局使用一个对象
     */
    GLOBAL,
    /**
     * 每个模块一个对象
     */
    MODULE,
    /**
     * 每次生成新对象
     */
    ONE;
}
