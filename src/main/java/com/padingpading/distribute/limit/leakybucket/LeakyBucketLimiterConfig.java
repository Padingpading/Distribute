package com.padingpading.distribute.limit.leakybucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author libin
 * @description
 * @date 2021/9/18
 */
@Configuration
public class LeakyBucketLimiterConfig {

    @Bean
    public LeakyBucketLimiter getLeakyBucketLimiter(){
        LeakyBucketLimiter leakyBucketLimiter = new LeakyBucketLimiter(3,200);
        return leakyBucketLimiter;
    }

}
