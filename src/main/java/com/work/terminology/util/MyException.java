package com.work.terminology.util;

import lombok.Getter;

@Getter
public class MyException extends Exception{

    private int error_code = 0;

//    public MyException(String msg, int code, Throwable throwable){
//        super(msg,throwable);
//        error_code = code;
//    }

    public MyException(ResultCode resultCode, Throwable throwable) {
        super(resultCode.getMsg(),throwable);
        error_code = resultCode.getCode();
    }
}


