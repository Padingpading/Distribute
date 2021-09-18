package com.padingpading.distribute.limit.controller;

import com.padingpading.distribute.limit.count.CountLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/limit")
public class LimitController {

    @Autowired
    private CountLimiter CountLimiter;

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

}
