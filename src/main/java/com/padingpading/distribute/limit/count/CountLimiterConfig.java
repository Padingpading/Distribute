package com.padingpading.distribute.limit.count;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author libin
 * @description
 * @date 2021/9/18
 */
@Configuration
public class CountLimiterConfig {

    @Bean
    public CountLimiter getCountLimiter(){
        CountLimiter countLimiter = new CountLimiter(1,2);
        return countLimiter;
    }
}
