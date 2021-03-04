package com.opsnow.terminology.service;

import com.google.gson.JsonArray;
import com.opsnow.terminology.model.OauthParameter;
import com.opsnow.terminology.model.Parameter;
import com.opsnow.terminology.model.SheetParameter;
import com.opsnow.terminology.model.Terminology;
import com.opsnow.terminology.util.MyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            result.put("code", 200);
            result.put("msg", "success");

            log.info("{} end", Thread.currentThread().getStackTrace()[1].getMethodName());

        } catch (MyException e){
            log.info("",e);
            result.put("code",e.getError_code());
            result.put("msg",e.getMessage());
        }

        return result;
    }
}
