package com.supereal.bigfile.common.singleton;

import lombok.Data;

/**
 * @author 记录上传文件的index以及设置时间
 */
@Data
public class FileIndex {

    private Integer index;

    private Long timestamp;
}
