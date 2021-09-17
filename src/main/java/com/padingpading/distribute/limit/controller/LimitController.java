package com.padingpading.distribute.limit.controller;

import com.padingpading.distribute.limit.redis.Limit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author libin
 * @description
 * @date 2021/9/17
 */
@RestController("/limit")
public class LimitController {

    @GetMapping("/redis")
    @Limit(period = 5,count = 3)
    public String redisLimit(@RequestParam String param){
        return "";
    }
}
