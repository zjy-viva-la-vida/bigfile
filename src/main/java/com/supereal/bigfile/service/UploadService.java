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
     * 保存
     * @param form
     * @param saveDirectory
     * @param status
     * @param fileIndex
     */
    void saveUploadFile(FileForm form, String saveDirectory,Integer status,Integer fileIndex);


    /**
     * 校验分片文件是否上传过
     * @param form
     * @param combineFlag
     * @return
     */
    Result checkPartFileIsExist(FileForm form,boolean combineFlag);

    /**
     * 上分片文件
     * @param form 文件表单信息
     * @return
     */
    Result realUpload(FileForm form);

    /**
     * 开始合并分片文件，合并前会做校验
     * @param form
     * @return
     */
    Result combineAllFile(FileForm form);

    /**
     * 从队列中读取数据，上传文件，暂时未用到
     * @return
     */
    Result realUploadByQueue();


}
