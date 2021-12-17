package com.hack2win.dynamicrabbits.cache;

import com.alibaba.fastjson.JSONObject;
import com.hack2win.dynamicrabbits.dynamic.entity.Strategy;
import com.hack2win.dynamicrabbits.dynamic.redis.RedisTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 本地缓存管理工具类
 * @Author: zhangqingbiao
 * @Date: 2021/12/11 23:04
 * @Version: 1.0
 */
@Component
public class ConcurrentHashMapCacheUtils {
    private Logger LOGGER = LoggerFactory.getLogger(ConcurrentHashMapCacheUtils.class);

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private FlushCacheThread flushCacheThread;
    /**
     * 时间2s
     */
    final Long TWO_SECOND = 2 * 1000L;
    /**
     * 缓存对象
     */
    private final Map<String, List<Strategy>> CACHE_OBJECT_MAP = new ConcurrentHashMap<>();
    /**
     * 清理过期缓存是否在运行
     */
    private volatile Boolean CLEAN_THREAD_IS_RUN = false;

    /**
     * 设置缓存
     */
    public void setCache(String cacheKey, List<Strategy> strategy) {
        CACHE_OBJECT_MAP.put(cacheKey, strategy);
        LOGGER.info("have set key :" + cacheKey);
    }

    /**
     * 刷新缓存
     */
    public void refresh() {
        // 刷新缓存
        Map<String, List<Strategy>> allCache = getAllCache();
        if (null != allCache) {
            allCache.keySet().forEach(r -> {
                String result = redisTemplate.queryCacheByKey(r);
                System.out.println("result:" + result);
                if (StringUtils.isBlank(result)) {
                    removeCacheKey(r);
                } else {
                    List<Strategy> strategies = JSONObject.parseArray(result, Strategy.class);
                    setCache(r, strategies);
                }
            });
        }
    }

    /**
     * 获取缓存
     */
    public List<Strategy> getCache(String cacheKey) {
        if (!CACHE_OBJECT_MAP.containsKey(cacheKey)) {
            CLEAN_THREAD_IS_RUN = false;
            CACHE_OBJECT_MAP.put(cacheKey, Collections.emptyList());
        }
        startCleanThread();
        if (checkCache(cacheKey)) {
            return CACHE_OBJECT_MAP.get(cacheKey);
        }
        return Collections.emptyList();
    }

    /**
     * 获取所有的缓存key
     */
    public Map<String, List<Strategy>> getAllCache() {
        return CACHE_OBJECT_MAP;
    }

    /**
     * 删除所有缓存
     */
    public void clear() {
        LOGGER.info("have clean all key !");
        CACHE_OBJECT_MAP.clear();
    }

    /**
     * 删除指定的key
     */
    public void removeCacheKey(String key) {
        LOGGER.info("have clean one key !");
        CACHE_OBJECT_MAP.remove(key);
    }


    /**
     * 判断缓存在不在
     */
    private boolean checkCache(String cacheKey) {
        if (CollectionUtils.isEmpty(CACHE_OBJECT_MAP.get(cacheKey))) {
            return false;
        }
        return true;
    }

    /**
     * 设置清理线程的运行状态为正在运行
     */
    void setCleanThreadRun() {
        CLEAN_THREAD_IS_RUN = true;
    }

    /**
     * 设置清理线程的运行状态为种子
     */
    void setCleanThreadStop() {
        CLEAN_THREAD_IS_RUN = false;
    }

    /**
     * 开启刷新缓存的线程
     */
    private void startCleanThread() {
        if (!CLEAN_THREAD_IS_RUN) {
            Thread thread = new Thread(flushCacheThread);
            //设置为后台守护线程
            thread.setDaemon(true);
            thread.start();
        }
    }


}