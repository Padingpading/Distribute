package com.padingpading.distribute.redis.controller;

import com.padingpading.distribute.redis.huadong.HuadongChuangkou;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author libin
 * @description
 * @date 2021/10/20
 */
@RestController
@RequestMapping("/redis")
public class TestHuadongController {

    @Autowired
    private HuadongChuangkou huadongChuangkou;


    @GetMapping("/huaddong")
    public Boolean testCountLimiter(@RequestParam String param){
        boolean dfdfdf = huadongChuangkou.isActionAllowed("123456", "dfdfdf", 60, 5);
        return dfdfdf;
    }

    @GetMapping("/addKey")
    public Boolean addKey(@RequestParam String param){
        boolean dfdfdf = huadongChuangkou.addKey("123456", "dfdfdf");
        return dfdfdf;
    }


}
