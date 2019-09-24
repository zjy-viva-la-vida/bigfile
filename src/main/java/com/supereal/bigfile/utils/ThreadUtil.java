package com.supereal.bigfile.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: ccos-manager-w
 * @description:
 * @author: lei.xu
 * @create: 2019-06-27 10:30
 **/
public class ThreadUtil {
    private ThreadUtil(){}

    //后面有需要在优化
    public static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void run(Runnable runnable){
        executorService.execute(runnable);
    }
}
