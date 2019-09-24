package com.supereal.bigfile.exception;


import com.supereal.bigfile.common.ErrorCode;

/**
 * @program: ccos-manager
 * @description: 校验异常
 * @author: lei.xu
 * @create: 2019-03-20 10:06
 **/
public class BusinessException extends BaseException {
    public BusinessException(String message) {
        super(ErrorCode.BUSINESS_FAIL.getCode(), message);
    }

    public BusinessException(int code) {
        super(code, "业务异常");
    }

    public BusinessException(int code, String message) {
        super(code, message);
    }
}
