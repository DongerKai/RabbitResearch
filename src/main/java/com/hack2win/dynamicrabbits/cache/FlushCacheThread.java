package com.hack2win.dynamicrabbits.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: 每2s清理一次所有缓存
 * @Author: zhangqingbiao
 * @Date: 2021/12/11 23:06
 * @Version: 1.0
 */
@Component
public class FlushCacheThread implements Runnable {

    @Autowired
    private ConcurrentHashMapCacheUtils concurrentHashMapCacheUtils;
    @Override
    public void run() {
        System.out.println("---------");
        concurrentHashMapCacheUtils.setCleanThreadRun();
        while (true) {
            System.out.println("clean thread run ");

            concurrentHashMapCacheUtils.refresh();
            try {
                Thread.sleep(concurrentHashMapCacheUtils.TWO_SECOND);
            } catch (InterruptedException e) {
                e.printStackTrace();
                concurrentHashMapCacheUtils.setCleanThreadStop();
            }
        }
    }
}
