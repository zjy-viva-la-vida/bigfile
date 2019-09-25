package com.supereal.bigfile.controller;


import com.supereal.bigfile.common.Constant;
import com.supereal.bigfile.form.FileForm;
import com.supereal.bigfile.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 设备资源管理接口
 *
 * @author qisong.li@bitmain.com
 * @version 1.0.0
 * @since 2019/03/11
 */
@RequestMapping("/test")
@RestController
@Slf4j
public class TestController {


    @GetMapping("/testThread")
    @ResponseBody
    public String testThread2(){
        ThreadPoolExecutor startThreadPool = new ThreadPoolExecutor(15, 100, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        log.info("启动线程前，当前线程总数为：" + bean.getThreadCount());
        startThreadPool.execute(()->{
            for(int i=1;i<=10;i++){
                try {
                    Thread.sleep(2000);
                }catch (Exception e){

                }
                log.info("第 " + i + " 次执行，当前线程总数为：" + bean.getThreadCount());
            }
        });

        //判断线程是否执行完毕
        startThreadPool.shutdown();
        try{
            while (true) {
                if (startThreadPool.isTerminated()) {
                    log.info("批量启动实时任务线程执行中....../////......");
                    break;
                }
                Thread.sleep(200);
            }
        }catch (Exception e){
            log.error("停止批量启动实时任务线程出错：" + e.getMessage());
        }
        log.info("启动线程后，当前线程总数为：" + bean.getThreadCount());
        return "当前线程总数为：" + bean.getThreadCount();

    }


    @GetMapping("/open")
    public ModelAndView open() {

        return new ModelAndView("test");
    }

    @PostMapping("/uploadTest")
    public Result upload(@Valid FileForm form, @RequestParam(value = "file", required = false) MultipartFile multipartFile) {
        try {
            multipartFile.transferTo(new File(Constant.PATH,form.getName()));
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("上传失败：" + e.getMessage());
        }

    }


}
