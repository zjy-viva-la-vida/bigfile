package com.supereal.bigfile.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.supereal.bigfile.Singleton.FileSingleton;
import com.supereal.bigfile.common.Constant;
import com.supereal.bigfile.common.ErrorCode;
import com.supereal.bigfile.dataobject.UploadFile;
import com.supereal.bigfile.exception.BusinessException;
import com.supereal.bigfile.form.FileForm;
import com.supereal.bigfile.repository.UploadFileRepository;
import com.supereal.bigfile.service.UploadService;
import com.supereal.bigfile.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Create by tianci
 * 2019/1/11 11:24
 * @author bitmain
 */

@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

    @Autowired
    UploadFileRepository uploadFileRepository;

    @Override
    public Result findByFileMd5(FileForm form) {
        /*UploadFile uploadFile = uploadFileRepository.findByFileMd5(form.getMd5());*/

        UploadFile uploadFile = uploadFileRepository.findUploadFileByName(form.getName());

        String fileId = KeyUtil.genUniqueKey();
        if(uploadFile != null){
            fileId = uploadFile.getId();
        }

        JSONObject json = new JSONObject();
        json.put("fileId",fileId);
        json.put("date", simpleDateFormat.format(new Date()));
        return Result.ok(json);
    }

    @Override
    public Result checkPartFileIsExist(FileForm form,boolean combineFlag){
        log.info("跳过校验,index:" + form.getIndex());
        return Result.ok("跳过校验");
//        try{
//            long start = System.currentTimeMillis();
//            log.info("检验分片文件index:" + form.getIndex() + ",start:" + start);
//            String fileId = form.getFileId();
//            String partMd5 = form.getPartMd5();
//            Integer index = Integer.parseInt(form.getIndex());
//
//            //文件存储位置
//            String saveDirectory = Constant.PATH + File.separator + fileId;
//            File file = new File(saveDirectory, fileId + "_" + index);
//            if(!file.exists()){
//                long end = System.currentTimeMillis();
//                log.info("检验分片文件1111index:" + form.getIndex() + ",end:" + end);
//                log.info("完成校验1111index:" + form.getIndex() + "，耗时：" + ( end - start )+ "毫秒");
//                return Result.ok("分片文件不存在，可以直接上传");
//            }
//            //获取分片文件的md5
//            String md5Str = FileMd5Util.getFileMD5(file);
//            if (md5Str != null && md5Str.length() == 31) {
//                log.info("前端传过来的文件md5:" + partMd5 + ",获取本地文件的md5:" + md5Str);
//                md5Str = "0" + md5Str;
//            }
//            if(combineFlag){
//                //异步拼接所有文件任务
//                ThreadUtil.run(() ->{
//                    //如果所有分片文件都存在，则执行合并分片文件操作
//                    File allFile = new File(saveDirectory);
//                    File[] fileArray = allFile.listFiles();
//                    int fileCount = fileArray == null ? 0 : fileArray.length;
//                    log.info("校验文件是否存在，fileCount:" + fileCount + ",total：" + form.getTotal() + ",index:" + form.getIndex());
//                    if (fileCount > 0 && fileCount == Integer.parseInt(form.getTotal())) {
//                        log.info("检验文件后开始合并文件");
//                        combineAllFile(form);
//                    }
//                });
//            }
//            if (md5Str != null && md5Str.equals(partMd5)) {
//                //分片已上传过
//                long end = System.currentTimeMillis();
//                log.info("检验分片文件2222index:" + form.getIndex() + ",end:" + end);
//                log.info("完成校验222index:" + form.getIndex() + "，耗时：" + ( end - start )+ "毫秒");
//                return Result.error("该分片文件已上传过");
//            } else {
//                //分片未上传
//                long end = System.currentTimeMillis();
//                log.info("检验分片文件33333index:" + form.getIndex() + ",end:" + end);
//                log.info("完成校验333index:" + form.getIndex() + "，耗时：" + ( end - start )+ "毫秒");
//                return Result.ok("该分片文件未上传过，可以执行上传");
//            }
//        }catch (Exception e){
//            //校验文件分片发生异常，直接让重新上传
//            log.error("校验文件分片发生异常，直接让重新上传,e:" + e.getMessage());
//            return Result.ok("校验文件分片出错，允许直接上传文件");
//        }

    }


    @Override
    public  Result realUpload(FileForm form, MultipartFile multipartFile) {
        long start = System.currentTimeMillis();
        String fileId = form.getFileId();
        Integer index = Integer.valueOf(form.getIndex());

        String md5 = form.getMd5();
        Integer total = Integer.valueOf(form.getTotal());
        String fileName = form.getName();
        String size = form.getSize();
        String suffix = NameUtil.getExtensionName(fileName);

        String saveDirectory = Constant.PATH + File.separator + fileId;
        String filePath = saveDirectory + File.separator + fileId + "." + suffix;
        //验证路径是否存在，不存在则创建目录
        File allFile = new File(saveDirectory);
        if (!allFile.exists()) {
            allFile.mkdirs();
        }
        //文件分片位置
        File file = new File(saveDirectory, fileId + "_" + index);


        /*Result checkResult = checkPartFileIsExist(form,false);
        if(Objects.equals(checkResult.getCode(), ErrorCode.RESULT_SUCCESS.getCode())){*/
            //分片上传过程中出错,有残余时需删除分块后,重新上传
            if (file.exists()) {
                file.delete();
            }
            //修改FileRes记录为上传成功
            UploadFile uploadFile = new UploadFile();
            uploadFile.setId(fileId);
            uploadFile.setStatus(1);
            uploadFile.setName(fileName);
            uploadFile.setFileMd5(md5);
            uploadFile.setSuffix(suffix);
            uploadFile.setPath(filePath);
            uploadFile.setSize(size);
            uploadFile.setDeleted(0);
            uploadFile.setTotalBlock(total);
            uploadFile.setCreateTime(new Date());
            FileSingleton fileSingleton = FileSingleton.getInstance();
            fileSingleton.setFileIdsIndex(fileId);
            uploadFile.setFileIndex(fileSingleton.getFileIdsIndex(fileId));
            uploadFileRepository.save(uploadFile);

            try{
                multipartFile.transferTo(new File(saveDirectory, fileId + "_" + index));
                if(Objects.equals(fileSingleton.getFileIdsIndex(fileId),total)){
                    //开一个线程单独去执行合并文件操作
                    ThreadUtil.run(() ->{
                        log.info("开始合并文件，已上传文件数：" + fileSingleton.getFileIdsIndex(fileId));
                        fileSingleton.removeFileId(fileId);
                        log.info("开始合并文件，删除单例数据后，已上传文件数：" + fileSingleton.getFileIdsIndex(fileId));
                        combineAllFile(form);
                    });
                }
                long end = System.currentTimeMillis();
                log.info("完成上传分片111，index:" + form.getIndex() + ",total:" + total +
                        ",已上传文件数：" + fileSingleton.getFileIdsIndex(fileId) + ",耗时：" + ( end - start )+ "毫秒");
                return Result.ok("分片文件上传成功！");
            }catch (Exception e){
                ExceptionRes res = ExceptionResponseUtil.spliceMsgFromException(e);
                log.error("存储分片文件出错：" + res.getMsg());
                return Result.ok("存储分片文件出错：" + res.getMsg());
            }

        /*}else{
            combineAllFile(form);
            long end = System.currentTimeMillis();
            System.out.println("完成上传分片222index:" + form.getIndex() + "，耗时：" + ( end - start )+ "毫秒");
            return Result.ok("分片文件已存在！");
        }*/

    }

    public Result combineAllFile(FileForm form) {

        try{
            String fileName = form.getName();
            String suffix = NameUtil.getExtensionName(fileName);

            String saveDirectory = Constant.PATH + File.separator + form.getFileId();
            String finalFilePath = saveDirectory + File.separator + form.getFileId() + "." + suffix;


            Integer total = Integer.valueOf(form.getTotal());
            File finalFile = new File(finalFilePath);
            if (finalFile.exists()) {
                finalFile.delete();
            }
            File allFile = new File(saveDirectory);
            File[] fileArray = allFile.listFiles();
            int fileCount = fileArray == null ? 0 : fileArray.length;

            log.info("尝试合并文件，index：" + form.getIndex() + "，fileCount：" + fileCount);
            if (fileCount > 0 && fileCount == total) {
                log.info("获取fileId:" + form.getFileId() + ",下的文件列表总数:" + fileCount + ",和total:" + total + "，开始合并文件");
                //分块全部上传完毕,合并
                File newFile = new File(saveDirectory, form.getFileId() + "." + suffix);
                //文件追加写入
                FileOutputStream outputStream = new FileOutputStream(newFile, true);
                byte[] byt = new byte[20 * 1024 * 1024];
                int len;
                //分片文件
                FileInputStream temp = null;
                for (int i = 0; i < total; i++) {
                    int j = i + 1;
                    temp = new FileInputStream(new File(saveDirectory, form.getFileId() + "_" + j));
                    while ((len = temp.read(byt)) != -1) {
                        outputStream.write(byt, 0, len);
                    }
                }
                //关闭流
                temp.close();
                outputStream.close();
                //修改FileRes记录为上传成功
                UploadFile uploadFile = new UploadFile();
                uploadFile.setId(form.getFileId());
                uploadFile.setStatus(2);
                uploadFile.setName(form.getName());
                uploadFile.setFileMd5(form.getMd5());
                uploadFile.setSuffix(suffix);
                uploadFile.setPath(finalFilePath);
                uploadFile.setSize(form.getSize());
                uploadFile.setDeleted(0);
                uploadFile.setTotalBlock(total);
                uploadFile.setFileIndex(total);
                uploadFileRepository.save(uploadFile);
                return Result.ok("所有分片上传完成，文件合并成功");

            } else {
                log.error("获取fileId:" + form.getFileId() + ",下的文件为空，或该文件列表总数:" + fileCount + ",和total:" + total + "不一致，不执行合并文件");
                return Result.error("获取fileId:" + form.getFileId() + ",下的文件为空，或该文件列表总数:" + fileCount + ",和total:" + total + "不一致");
            }
        }catch (Exception e){
            ExceptionRes res = ExceptionResponseUtil.spliceMsgFromException(e);
            throw new BusinessException("合并文件出错：" + res.getMsg());
        }

    }
}
