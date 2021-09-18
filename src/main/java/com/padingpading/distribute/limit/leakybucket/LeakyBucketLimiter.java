package com.padingpading.distribute.limit.leakybucket;

/**
 * @author libin
 * @description 漏桶算法。
 * @date 2021/9/18
 */

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 漏桶算法
 */
public class LeakyBucketLimiter {
    /**
     * 漏桶流出速率(多少纳秒执行一次)
     */
    private long outflowRateNanos;

    /**
     * 漏桶容器
     */
    private volatile BlockingQueue<Drip> queue;

    /**
     * 滴水线程
     */
    private Thread outflowThread;

    /**
     * 水滴
     */
    private static class Drip {
        /**
         * 业务主键
         */
        private String busId;

        /**
         * 水滴对应的调用者线程
         */
        private Thread thread;

        public Drip(String busId, Thread thread) {
            this.thread = thread;
        }

        public String getBusId() {
            return this.busId;
        }

        public Thread getThread() {
            return this.thread;
        }
    }

    /**
     * @param second 秒
     * @param time   调用次数
     */
    public LeakyBucketLimiter(int second, int time) {
        if (second <= 0 || time <= 0) {
            throw new IllegalArgumentException("second and time must by positive");
        }
        //多少纳秒执行一次
        outflowRateNanos = TimeUnit.SECONDS.toNanos(second) / time;
        queue = new LinkedBlockingQueue<>(time);

        outflowThread = new Thread(() -> {
            while (true) {
                Drip drip = null;
                try {
                    // 阻塞，直到从桶里拿到水滴
                    drip = queue.take();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (drip != null && drip.getThread() != null) {
                    // 唤醒阻塞的水滴里面的线程
                    LockSupport.unpark(drip.getThread());
                }
                // 休息一段时间，开始下一次滴水
                LockSupport.parkNanos(this, outflowRateNanos);
            }
        }, "漏水线程");
        outflowThread.start();
    }

    /**
     * 业务请求
     *
     * @return
     */
    public boolean acquire(String busId) {
        Thread thread = Thread.currentThread();
        Drip drip = new Drip(busId, thread);
        //添加失败返回false。
        if (this.queue.offer(drip)) {
            LockSupport.park();
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //1秒限制执行1次
        LeakyBucketLimiter leakyBucketLimiter = new LeakyBucketLimiter(5, 2);
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String busId = "[业务ID：" + LocalDateTime.now().toString() + "]";
                    if (leakyBucketLimiter.acquire(busId)) {
                        System.out.println(LocalDateTime.now() + " " + Thread.currentThread().getName() + "：调用外部接口...成功：" + busId);
                    } else {
                        System.out.println(LocalDateTime.now() + " " + Thread.currentThread().getName() + "：调用外部接口...失败：" + busId);
                    }
                }
            }, "测试线程-" + i).start();
            TimeUnit.MILLISECONDS.sleep(500);
        }
    }
}