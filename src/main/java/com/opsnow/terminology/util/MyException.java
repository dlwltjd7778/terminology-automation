package com.opsnow.terminology.util;

import lombok.Getter;

@Getter
public class MyException extends Exception{

    private int error_code = 0;

    public MyException(String msg, int code, Throwable throwable){
        super(msg,throwable);
        error_code = code;
    }


}


