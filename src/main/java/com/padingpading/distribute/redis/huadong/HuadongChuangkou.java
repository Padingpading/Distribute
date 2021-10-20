package com.padingpading.distribute.redis.huadong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author libin
 * @description
 * @date 2021/10/20
 */
@Service
public class HuadongChuangkou {


    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    public boolean addKey(String key, String value) {
        long current = System.currentTimeMillis();
        redisTemplate.opsForZSet().add(key, value, current);
        return true;
    }

    public boolean isActionAllowed(String userId, String actionKey, int period, int maxCount) {
        String key = String.format("hist:%s:%s", userId, actionKey);
        long current = System.currentTimeMillis();
        // 记录行为
        redisTemplate.opsForZSet().add(key, current, current);
        // 移除时间窗口之前的行为记录，剩下的都是时间窗口内的
        redisTemplate.opsForZSet().removeRangeByScore(key, 0, current - period * 1000);
        // 获取窗口内的行为数量
        Long zCard = redisTemplate.opsForZSet().zCard(key);
        // 设置zset过期时间，避免冷用户持续占用内存
        // 过期时间应该等于时间窗口长度，再多宽限1s
        redisTemplate.expire(key, period + 1, TimeUnit.SECONDS);
        return zCard <= maxCount;
    }

}
