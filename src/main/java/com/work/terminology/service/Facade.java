package com.work.terminology.service;

import com.google.common.base.Stopwatch;
import com.google.gson.JsonArray;
import com.work.terminology.model.OauthParameter;
import com.work.terminology.model.Parameter;
import com.work.terminology.model.SheetParameter;
import com.work.terminology.model.Terminology;
import com.work.terminology.util.MyException;
import com.work.terminology.util.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class Facade {

    @Autowired
    private GoogleAPIService googleAPI;
    @Autowired
    private TerminologyService terminologyService;

    /*
        method :       facade
        Parameter :    Parameter ( OauthParameter, SheetParameter 포함하는 model )
        Return :       Map<String,Object>
        desc :         코드와 메세지를 리턴하는 Facade
    */
    public Map<String, Object> facade(Parameter parameter) {

        final Stopwatch stopwatch = Stopwatch.createStarted();

        log.info("{} start", Thread.currentThread().getStackTrace()[1].getMethodName());
        Map<String, Object> result = new HashMap<>();

        try{
            OauthParameter oauthParameter = parameter.getOauth_parameter();
            SheetParameter sheetParameter = parameter.getSheet_parameter();

            String access_token = googleAPI.getAccessTokenByRefreshToken(oauthParameter);

            // 1. 데이터를 받아오는 부분
            sheetParameter.setAccess_token(access_token);
            String data = googleAPI.getSheetDataByToken(sheetParameter);

            // 2. 데이터를 파싱하는 부분 ( 컬럼명과 시트데이터 가져오는 부분 )
            JsonArray jsonArray = terminologyService.parseData(data);

            // 3. 가져온 데이터를 VO에 매핑하기 위한 작업들
            List<Terminology> dataList = terminologyService.mappingData(jsonArray);

            // 4. 데이터 DB에 저장
            terminologyService.saveData(dataList);

            // 5. 프로시저 실행
            terminologyService.callProcedure();

            result.put("code", ResultCode.Success.getCode());
            result.put("msg", ResultCode.Success.getMsg());

            log.info("{} end : {}ms", Thread.currentThread().getStackTrace()[1].getMethodName(), stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));

        } catch (MyException e){
            log.info("",e);
            result.put("code",e.getError_code());
            result.put("msg",e.getMessage());
        }

        return result;
    }
}
