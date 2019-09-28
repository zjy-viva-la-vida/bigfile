package com.supereal.bigfile.repository;

import com.supereal.bigfile.entity.UploadFile;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Create by tianci
 * 2019/1/10 15:02
 */
public interface UploadFileRepository extends JpaRepository<UploadFile, String> {

    UploadFile findByFileMd5(String fileMd5);


    @Cacheable(value="findUploadFileById", sync=true)
    UploadFile findUploadFileById(String id);

    UploadFile findUploadFileByNameAndStatus(String fileName,Integer status);

    UploadFile findUploadFileByName(String fileName);

    void deleteByFileMd5(String fileMd5);
}
