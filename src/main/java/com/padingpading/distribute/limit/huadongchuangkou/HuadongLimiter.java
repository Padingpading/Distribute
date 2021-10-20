package com.padingpading.distribute.limit.huadongchuangkou;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author libin
 * @description redis 滑动窗口限流。
 * @date 2021/10/20
 */
@Service
public class HuadongLimiter {

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    private static final String LIMIT_KEY = "LIMIT:";

    public boolean isActionAllowed(String key, String value, int period, int maxCount) {
        String rediskey = LIMIT_KEY + key;
        long current = System.currentTimeMillis();
        // 记录行为
        redisTemplate.opsForZSet().add(rediskey, current, current);
        // 移除时间窗口之前的行为记录，剩下的都是时间窗口内的
        redisTemplate.opsForZSet().removeRangeByScore(rediskey, 0, current - period * 1000);
        // 获取窗口内的行为数量
        Long zCard = redisTemplate.opsForZSet().zCard(rediskey);
        // 设置zset过期时间，避免冷用户持续占用内存
        // 过期时间应该等于时间窗口长度，再多宽限1s
        redisTemplate.expire(rediskey, period + 1, TimeUnit.SECONDS);
        return zCard <= maxCount;
    }

}
