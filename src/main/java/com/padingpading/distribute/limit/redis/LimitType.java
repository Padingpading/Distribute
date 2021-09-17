package com.padingpading.distribute.limit.redis;

/**
 * 限流枚举
 */
public enum LimitType {
    // 传统类型
    CUSTOMER,
    // 根据 IP 限制
    IP;
}
