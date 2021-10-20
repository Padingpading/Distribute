package com.padingpading.distribute;

import com.padingpading.distribute.redis.huadong.HuadongChuangkou;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DistributeApplicationTests {

    @Autowired
    private HuadongChuangkou huadongChuangkou;

    @Test
    void contextLoads() {
        boolean b = huadongChuangkou.addKey("", "");
    }

}
