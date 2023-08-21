package com.feige.fim.storage;


import com.feige.fim.api.SessionStorage;
import com.feige.fim.config.ClientConfigKey;
import com.feige.utils.common.AssertUtil;
import com.feige.framework.annotation.InitMethod;
import com.feige.framework.annotation.SpiComp;
import com.feige.framework.annotation.Value;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SpiComp(interfaces = SessionStorage.class)
public class FileSessionStorage implements SessionStorage {
    public static final String FILE_NAME = "client.dat";
    private final Map<String, String> sessionMap = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    @Value(value = ClientConfigKey.FILE_SESSION_STORAGE_PATH_KEY)
    private String path;

    public void writeSession(Map<String, String> copyMap) {
        File file = new File(path ,FILE_NAME);
        if (!file.exists()){
            boolean mkdir = file.getParentFile().mkdir();
            try {
                boolean newFile = file.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException("create file failure", e);
            }
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))){
            oos.writeObject(copyMap);
        } catch (Exception e) {
            throw new RuntimeException("write session failure", e);
        }
    }

    @InitMethod
    public void readSession() {
        File file = new File(path ,FILE_NAME);
        if (!file.exists()){
            boolean mkdir = file.getParentFile().mkdir();
            try {
                boolean newFile = file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("create file failure", e);
            }
        }
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(file);
            if (fis.available() > 0) {
                ois = new ObjectInputStream(fis);
                Map<String, String> tempMap = (Map<String, String>) ois.readObject();
                this.sessionMap.putAll(tempMap);
            }
        } catch (Exception e) {
            throw new RuntimeException("read session failure", e);
        }finally {
            if (ois != null){
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void setItem(String key, String value) {
        AssertUtil.notBlank(key, "key");
        AssertUtil.notBlank(value, "value");
        writeLock.lock();
        try {
            Map<String, String> copyMap = new HashMap<>(sessionMap);
            copyMap.put(key, value);
            writeSession(copyMap);
            sessionMap.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public String getItem(String key) {
        readLock.lock();
        try {
            return sessionMap.get(key);
        }finally {
            readLock.unlock();
        }
    }

    @Override
    public String removeItem(String key) {
        AssertUtil.notBlank(key, "key");
        writeLock.lock();
        try {
            Map<String, String> copyMap = new HashMap<>(sessionMap);
            copyMap.remove(key);
            writeSession(copyMap);
            return sessionMap.remove(key);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void clear() {
        writeLock.lock();
        try {
            File file = new File(path + File.separator + FILE_NAME);
            for (int i = 0; i < 10; i++) {
                if (file.delete()){
                    break;
                }
            }
            sessionMap.clear();
        } finally {
            writeLock.unlock();
        }
    }
}

