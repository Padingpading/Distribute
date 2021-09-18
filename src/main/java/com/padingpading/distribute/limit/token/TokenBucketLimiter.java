package com.padingpading.distribute.limit.token;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 令牌桶限流算法
 */
public class TokenBucketLimiter {
    /**
     * 桶的大小
     */
    private double bucketSize;

    /**
     * 桶里的令牌数
     */
    private double tokenCount;

    /**
     * 令牌增加速度(每毫秒)
     */
    private double tokenAddRateMillSecond;

    /**
     * 上次更新时间（毫秒）
     */
    private long lastUpdateTime;

    /**
     * @param second 秒
     * @param time   调用次数
     */
    public TokenBucketLimiter(double second, double time) {
        if (second <= 0 || time <= 0) {
            throw new IllegalArgumentException("second and time must by positive");
        }
        // 桶的大小
        this.bucketSize = time;
        // 桶里的令牌数
        this.tokenCount = this.bucketSize;
        // 令牌增加速度(每毫秒)
        this.tokenAddRateMillSecond = time / second / 1000;
        // 上次更新时间（毫秒）
        this.lastUpdateTime = System.currentTimeMillis();
    }

    /**
     * 刷新桶内令牌数(令牌数不得超过桶的大小)
     * 计算“上次刷新时间”到“当前刷新时间”中间，增加的令牌数
     */
    private void refreshTokenCount() {
        long now = System.currentTimeMillis();
        this.tokenCount = Math.min(this.bucketSize, this.tokenCount + ((now - this.lastUpdateTime) * this.tokenAddRateMillSecond));
        this.lastUpdateTime = now;
    }

    /**
     * 尝试拿到权限
     *
     * @return
     */
    public synchronized boolean tryAcquire() {
        // 刷新桶内令牌数
        this.refreshTokenCount();
        if ((this.tokenCount - 1) >= 0) {
            // 如果桶中有令牌，令牌数-1
            this.tokenCount--;
            return true;
        } else {
            // 桶中已无令牌
            return false;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TokenBucketLimiter leakyBucketLimiter = new TokenBucketLimiter(2, 1);
        for (int i = 0; i <= 10; i++) {
            System.out.println(LocalDateTime.now() + " " + leakyBucketLimiter.tryAcquire());
            TimeUnit.SECONDS.sleep(1);
        }
    }
}