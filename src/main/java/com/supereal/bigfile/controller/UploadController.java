package com.supereal.bigfile.controller;

import com.supereal.bigfile.form.FileForm;
import com.supereal.bigfile.service.UploadFileService;
import com.supereal.bigfile.service.UploadService;
import com.supereal.bigfile.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Map;

/**
 * Create by tianci
 * 2019/1/10 15:41
 */
@RestController
@RequestMapping("/uploadFile")
public class UploadController {

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
    public Result upload(@Valid FileForm form,@RequestParam(value = "data", required = false)MultipartFile multipartFile) {


        try {
            Result result = uploadService.realUpload(form, multipartFile);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("上传失败：" + e.getMessage());
        }

    }
}
