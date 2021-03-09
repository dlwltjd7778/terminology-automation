package com.opsnow.terminology.util;

import lombok.Getter;

@Getter
public enum ExceptionCode  {
    OAuthAPIException("Google Oauth API Error",401)
    , SheetAPIException("Google Sheet API Error",402)
    , ParsingDataException("Data Parsing Error",501)
    , MappingDataException("Data Mapping Error",502)
    , SaveDataException("Save Data Error",503)
    , CallProcedureException("Procedure Call Error",504);

    private String msg;
    private int code;

    private ExceptionCode(String msg, int code){
        this.msg = msg;
        this.code = code;
    }
}
