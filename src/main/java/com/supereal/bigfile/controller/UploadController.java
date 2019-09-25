package com.supereal.bigfile.controller;

import com.supereal.bigfile.vo.FileForm;
import com.supereal.bigfile.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Map;

/**
 * 刚类是最原始的方法，太复杂了
 * 2019/1/10 15:41
 * @author bitmain
 */
@RestController
@RequestMapping("/file")
public class UploadController {

    @Autowired
    UploadService uploadFileService;


    @GetMapping("/open")
    public ModelAndView open() {

        return new ModelAndView("original/upload");
    }

    @PostMapping("/isUpload")
    public Map<String, Object> isUpload(@Valid FileForm form) {

        return uploadFileService.findByFileMd5(form.getMd5());

    }

    @PostMapping("/upload")
    public Map<String, Object> upload(@Valid FileForm form,
                                      @RequestParam(value = "data", required = false)MultipartFile multipartFile) {
        Map<String, Object> map = null;

        try {
            map = uploadFileService.realUpload(form, multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
