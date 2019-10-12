package com.supereal.bigfile.service;

import com.supereal.bigfile.vo.FileForm;
import com.supereal.bigfile.utils.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * Create by tianci
 * 2019/1/11 11:23
 */
public interface UploadFileService {

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
     * @param fastPath
     */
    void saveUploadFile(FileForm form, String saveDirectory,Integer status,String fastPath);


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
     * @param flag
     * @return
     */
    Result combineAllFile(FileForm form,String flag);

    /**
     * 从队列中读取数据，上传文件，暂时未用到
     * @return
     */
    Result realUploadByQueue();


    /**
     * 测试直接上传文件到fastDfs
     * @param file
     * @param suffix
     * @return
     */
    Result uploadToFastDfs(MultipartFile file,String suffix);

    /**
     * 测试直接删除fastDfs文件
     * @param group
     * @param path
     * @return
     */
    Result deleteFile(String group,String path);

    /**
     * 测试网络文件是否存在
     * @param url
     * @return
     */
    boolean checkFastFileIsExist(String url);

}
