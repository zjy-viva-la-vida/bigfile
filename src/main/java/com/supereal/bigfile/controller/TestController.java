package com.supereal.bigfile.controller;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.supereal.bigfile.annotation.PassToken;
import com.supereal.bigfile.common.Constant;
import com.supereal.bigfile.entity.UploadFile;
import com.supereal.bigfile.repository.UploadFileRepository;
import com.supereal.bigfile.service.UploadFileService;
import com.supereal.bigfile.vo.FileForm;
import com.supereal.bigfile.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

    @Autowired
    UploadFileRepository uploadFileRepository;

    @Resource
    UploadFileService uploadFileService;


    /**
     * 测试网络文件是否存在
     * @param url
     */
    @GetMapping("/checkFile")
    @ResponseBody
    public Result checkFastFileIsExist(@RequestParam("url")String  url){
        return Result.ok(uploadFileService.checkFastFileIsExist(url));
    }


    /**
     * 测试：直接删除fastdfs文件
     */
    @CrossOrigin
    @RequestMapping(value = "/deleteFile", method = RequestMethod.GET)
    @ResponseBody
    @PassToken
    public Result deleteFile(@RequestParam("group") @Validated @NotNull String group, @RequestParam("path") @Validated @NotNull String path)  {
        Result result = uploadFileService.deleteFile(group,path);
        return result;
    }


    /**
     * 测试缓存
     * @param id
     * @throws Exception
     */
    @GetMapping("/testCash")
    @ResponseBody
    public Result testCash(@RequestParam("id")String  id) throws Exception{
        log.info("第一次：从数据库中获取");
        UploadFile p = uploadFileRepository.findUploadFileById(id);
        log.info("1st time: {}", JSON.toJSON(p));

        log.info("第二次：从缓存中获取");
        p = uploadFileRepository.findUploadFileById(id);
        log.info("2nd time: {}", JSON.toJSON(p));
//
//        Thread.sleep(5000);
//
//        log.info("第三次：5秒钟超时后，从数据库中获取");
//        p = uploadFileRepository.findUploadFileById(id);
//        log.info("3rd time: {}", JSON.toJSON(p));
//
//        log.info("第四次：从缓存中获取");
//        p = uploadFileRepository.findUploadFileById(id);
//        log.info("4th time: {}", JSON.toJSON(p));
        return Result.ok(p);
    }


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

        return new ModelAndView("original/test");
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
