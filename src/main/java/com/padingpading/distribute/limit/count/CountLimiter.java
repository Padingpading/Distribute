package com.padingpading.distribute.limit.count;


import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;


/**
 * 计数法
 */
public class CountLimiter {
    /**
     * 执行区间(毫秒)
     */
    private int secondMill;

    /**
     * 区间内计数多少次
     */
    private int maxCount;

    /**
     * 当前计数
     */
    private int currentCount;

    /**
     * 上次更新时间（毫秒）
     */
    private long lastUpdateTime;

    public CountLimiter(int second, int count) {
        if (second <= 0 || count <= 0) {
            throw new IllegalArgumentException("second and time must by positive");
        }
        this.secondMill = second * 1000;
        this.maxCount = count;
        this.currentCount = this.maxCount;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    /**
     * 刷新计数器
     */
    private void refreshCount() {
        long now = System.currentTimeMillis();
        if ((now - this.lastUpdateTime) >= secondMill) {
            this.currentCount = maxCount;
            this.lastUpdateTime = now;
        }
    }

    /**
     * 获取授权
     * @return
     */
    public synchronized boolean tryAcquire() {
        // 刷新计数器
        this.refreshCount();
        if ((this.currentCount - 1) >= 0) {
            this.currentCount--;
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CountLimiter countLimiter = new CountLimiter(1,2);
        for (int i = 0; i < 10; i++) {
                System.out.println(LocalDateTime.now() + " " + countLimiter.tryAcquire());
                TimeUnit.MILLISECONDS.sleep(200);
            }
        }
}