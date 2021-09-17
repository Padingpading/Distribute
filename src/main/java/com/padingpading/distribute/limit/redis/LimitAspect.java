package com.padingpading.distribute.limit.redis;

import com.google.common.collect.Lists;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author libin
 * @description 限流aop
 * @date 2021/9/17
 */
@Aspect
@Component
public class LimitAspect {

    @Autowired
    private RedisTemplate<String, Serializable> limitRedisTemplate;

    private DefaultRedisScript<Long> redisScript;

    /**限流脚本
     *用的时候不超过阈值，则直接返回并执行计算器自加。
     */
    private static final String LUA_SCRIPT = "local c" +
            "\nc = redis.call('get',KEYS[1])" +
            "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
            "\nreturn c;" +
            "\nend" +
            "\nc = redis.call('incr',KEYS[1])" +
            "\nif tonumber(c) == 1 then" +
            "\nredis.call('expire',KEYS[1],ARGV[2])" +
            "\nend" +
            "\nreturn c;";


    @PostConstruct
    public void init() {
        redisScript = new DefaultRedisScript<>(LUA_SCRIPT);
        redisScript.setResultType(Long.class);
    }

    @Pointcut("@annotation(com.padingpading.distribute.limit.redis.Limit)")
    public void pointcut() {

    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String key = getRemoteHost(request);
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Limit limitAnnotation = method.getAnnotation(Limit.class);
        //限流模块
        String prefix = limitAnnotation.prefix();
        //限流时间
        int limitPeriod = limitAnnotation.period();
        //限流次数
        int limitCount = limitAnnotation.count();
        //获取脚本
        Number count = limitRedisTemplate.execute(redisScript, Lists.newArrayList(limitAnnotation.prefix() + ":", key), limitCount, limitPeriod);
        if (count != null && count.intValue() <= limitCount) {
            return point.proceed();
        } else {
            throw new RuntimeException("接口访问超出频率限制");
        }
    }

    public String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }
}
