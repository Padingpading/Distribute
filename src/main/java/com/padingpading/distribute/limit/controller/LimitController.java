package com.padingpading.distribute.limit.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.padingpading.distribute.limit.count.CountLimiter;
import com.padingpading.distribute.limit.huadongchuangkou.HuadongLimiter;
import com.padingpading.distribute.limit.leakybucket.LeakyBucketLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/limit")
public class LimitController {

    @Autowired
    private CountLimiter CountLimiter;

    @Autowired
    private LeakyBucketLimiter leakyBucketLimiter;

    @Autowired
    private HuadongLimiter huadongLimiter;

    RateLimiter rateLimiter = RateLimiter.create(4);


    @GetMapping("/redis")
    public String testLimit(@RequestParam String param){
        return "ffgfgf";
    }


    @GetMapping("/countLimiter")
    public String testCountLimiter(@RequestParam String param){
        if(!CountLimiter.tryAcquire()){
            return "被限流了";
        }
        return "CountLimiter";
    }

    @GetMapping("/leakyBucketLimiter")
    public String testLeakyBucketLimiter(@RequestParam String param){
        if(!leakyBucketLimiter.acquire(param)){
            return "被限流了";
        }

        return "leakyBucketLimiter";
    }

//    @GetMapping("/rateLimiter")
//    public String tesRateLimiter(@RequestParam String param){
//        if(!rateLimiter.acquire()){
//            return "被限流了";
//        }
//
//        return "leakyBucketLimiter";
//    }

    @GetMapping("/redis/huadong")
    public String huaDongLimit(@RequestParam String userId){
        if(!huadongLimiter.isActionAllowed(userId,"111",10,5)){
            return "被限流了";
        }
        return "success";
    }

}
