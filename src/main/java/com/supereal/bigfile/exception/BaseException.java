package com.supereal.bigfile.exception;

import lombok.Getter;

/**
 * @program: ccos
 * @description: 基本异常处理
 * @author: lei.xu
 * @create: 2019-03-18 19:52
 **/
@Getter
public class BaseException extends RuntimeException {

    private int code;

    public BaseException(int code, String message) {
        super(message);
        this.code = code;
    }

}
