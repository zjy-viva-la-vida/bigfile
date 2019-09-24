package com.supereal.bigfile.service;

import com.supereal.bigfile.form.FileForm;
import com.supereal.bigfile.utils.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Create by tianci
 * 2019/1/11 11:23
 */
public interface UploadService {

    /**
     * 通过md5值查找文件对象
     * @param form
     * @return
     */
    Result findByFileMd5(FileForm form);


    /**
     * @param form
     * @return
     */
    Result checkPartFileIsExist(FileForm form,boolean combineFlag);

    /**
     * 上传文件
     * @param form 文件表单信息
     * @param multipartFile 文件
     * @return
     */
    Result realUpload(FileForm form, MultipartFile multipartFile);


}
