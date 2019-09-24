package com.supereal.bigfile.exception;


import com.supereal.bigfile.common.ErrorCode;

/**
 * @program: ccos-manager
 * @description: 校验异常
 * @author: lei.xu
 * @create: 2019-03-20 10:06
 **/
public class ValidationException extends BaseException {


    public ValidationException(String message) {
        super(ErrorCode.INVALID_ARGUMENTS.getCode(), message);
    }

    public ValidationException(int code) {
        super(code, "校验有误");
    }

    public ValidationException(int code, String message) {
        super(code, message);
    }
}
