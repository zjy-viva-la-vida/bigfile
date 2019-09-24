package com.supereal.bigfile.utils;

import java.util.Random;
import java.util.UUID;

/**
 * Create by tianci
 * 2019/1/10 14:59
 */
public class KeyUtil {

    public static synchronized String genUniqueKey() {
        Random random = new Random();
        Integer num = random.nextInt(900000) + 100000;
       /* String uuid = System.currentTimeMillis() + String.valueOf(num);*/
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }
}
