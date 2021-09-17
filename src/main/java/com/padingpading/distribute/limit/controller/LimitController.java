package com.padingpading.distribute.limit.controller;

import com.padingpading.distribute.limit.redis.Limit;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/limit")
public class LimitController {

    @Limit(period = 5,count = 3,prefix="redis")
    @GetMapping("/redis")
    public String redisLimit(@RequestParam String param){
        return "ffgfgf";
    }
}
