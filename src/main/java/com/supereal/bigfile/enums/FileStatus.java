package com.supereal.bigfile.enums;


import lombok.AllArgsConstructor;

/**
 * 文件上传状态
 * @author bitmain
 */
@AllArgsConstructor
public enum FileStatus implements BaseEnum {

    /**
     *
     */
    UNKNOWN(-1, "未知"),
    NOT_FINISH(1, "文件分片未全部上传"),
    FINISH(2, "文件分片已全部上传完成");


    private int code;
    private String desc;


    @Override
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 通过code返回对应的描述信息
     *
     * @param code code
     * @return
     */
    public static String getDesc(Integer code) {
        for (FileStatus apiCodeEnum : FileStatus.values()) {
            if (code.equals(apiCodeEnum.getCode())) {
                return apiCodeEnum.getDesc();
            }
        }
        return UNKNOWN.getDesc();
    }
}
