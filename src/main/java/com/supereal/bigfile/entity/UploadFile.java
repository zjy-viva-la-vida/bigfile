package com.supereal.bigfile.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 *
 * 2019/1/10 14:38
 * @author bitmain
 */

@Entity
@Data
public class UploadFile {

    /**  uuid */
    @Id
    private String id;

    /**  文件路径 */
    private String path;

    /**fastDfs 文件存储位置*/
    private String fastPath;

    /**  文件大小 */
    private String size;

    /**  文件后缀 */
    private String suffix;

    /**  文件名字 */
    private String name;

    /**  文件md5 */
    private String fileMd5;

    /**  文件上传状态 */
    private Integer status;

    /**  文件上传状态 */
    private Integer deleted;

    /**  文件一共分成了多少块 */
    private Integer totalBlock;

    /**  文件上传到第几个块了，从1开始 */
    private Integer fileIndex;

    private Date createTime;

    private Date updateTime;
}
