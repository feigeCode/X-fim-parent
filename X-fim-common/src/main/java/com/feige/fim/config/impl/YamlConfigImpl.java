package com.feige.fim.config.impl;

import com.feige.fim.utils.YamlUtils;
import com.feige.api.config.Config;
import org.apache.commons.collections4.MapUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class YamlConfigImpl implements Config {
    private Map<String, Object> config;


    @Override
    public void parseFile(File file) throws Exception {
        this.config = YamlUtils.parser(new FileInputStream(file));
    }

    @Override
    public void parseFile(InputStream is) throws Exception {
        this.config = YamlUtils.parser(is);
    }

    @Override
    public Integer getInt(String key, Integer defaultValue) {
        return MapUtils.getInteger(this.config, key, defaultValue);
    }

    @Override
    public Integer getInt(String key) {
        return MapUtils.getInteger(this.config, key);
    }

    @Override
    public Long getLong(String key, Long defaultValue) {
        return MapUtils.getLong(this.config, key, defaultValue);
    }

    @Override
    public Long getLong(String key) {
        return MapUtils.getLong(this.config, key);
    }

    @Override
    public Double getDouble(String key, Double defaultValue) {
        return MapUtils.getDouble(this.config, key, defaultValue);
    }

    @Override
    public Double getDouble(String key) {
        return MapUtils.getDouble(this.config, key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return MapUtils.getString(this.config, key, defaultValue);
    }

    @Override
    public String getString(String key) {
        return MapUtils.getString(this.config, key);
    }

    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        return MapUtils.getBoolean(this.config, key, defaultValue);
    }

    @Override
    public Boolean getBoolean(String key) {
        return MapUtils.getBoolean(this.config, key);
    }

    @Override
    public Map<String, Object> getMap(String key) {
        return (Map<String, Object>) MapUtils.getMap(this.config, key);
    }

    @Override
    public List<String> getList(String key) {
        return (List<String>) MapUtils.getObject(this.config, key);
    }

    @Override
    public String[] getArr(String key) {
        return (String[]) MapUtils.getObject(this.config, key);
    }
}
