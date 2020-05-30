package com.zxshare.gmall.gmallcartweb;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallCartWebApplicationTests {

    @Test
    public void contextLoads() {
        //初始化
        BigDecimal bigDecimal = new BigDecimal(0.01f);
        BigDecimal bigDecimal1 = new BigDecimal(0.01d);
        BigDecimal bigDecimal2 = new BigDecimal("0.01");
        BigDecimal bigDecimal3 = new BigDecimal("6");
        BigDecimal bigDecimal4 = new BigDecimal("9");
        System.out.println(bigDecimal);
        System.out.println(bigDecimal1);
        System.out.println(bigDecimal2);

        //比较
        int i = bigDecimal1.compareTo(bigDecimal2); //1 0 -1
        System.out.println(i);

        //计算
             //+
        BigDecimal add = bigDecimal2.add(bigDecimal);
            //-
        BigDecimal subtract = bigDecimal2.subtract(bigDecimal);
                //*
        BigDecimal multiply = bigDecimal3.multiply(bigDecimal4);
         //除法
        BigDecimal divide = bigDecimal3.divide(bigDecimal4, 3, BigDecimal.ROUND_HALF_DOWN);
        //约数


    }

}
