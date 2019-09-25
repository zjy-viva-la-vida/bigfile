package com.supereal.bigfile.controller;

import com.supereal.bigfile.form.FileForm;
import com.supereal.bigfile.service.UploadService;
import com.supereal.bigfile.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Create by tianci
 * 2019/1/10 15:41
 */
@RestController
@RequestMapping("/uploadFile")
@Slf4j
public class UploadFileController {

    @Autowired
    UploadService uploadService;


    @GetMapping("/open")
    public ModelAndView open() {

        return new ModelAndView("uploadFile");
    }

    @PostMapping("/checkBeforeUpload")
    public Result isUpload(@Valid FileForm form) {

        return uploadService.findByFileMd5(form);

    }

    @PostMapping("/checkPartFile")
    public Result checkPartFile(@Valid FileForm form) {
        return uploadService.checkPartFileIsExist(form,true);

    }

    @PostMapping("/uploadPartFile")
    public Result upload(@Valid FileForm form) {
        try {
            Result result = uploadService.realUpload(form);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("上传失败：" + e.getMessage());
        }

    }
//
//    @PostMapping("/uploadPartFileToQueue")
//    public Result uploadPartFileToQueue(@Valid FileForm form) {
//        try {
//            FileSingleton fileSingleton = FileSingleton.getInstance();
//            fileSingleton.addFileFormToQueue(form);
//            if(fileSingleton.getFlag()){
//                fileSingleton.setFlag(false);
//                return uploadService.realUploadByQueue();
//            }else{
//                log.info("数据已放到队列中，未重复调用");
//            }
//            return Result.ok("文件上传中，请稍后");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Result.error("上传失败：" + e.getMessage());
//        }
//
//    }
}
