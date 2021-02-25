package com.opsnow.terminology.service;

import com.google.gson.JsonArray;
import com.opsnow.terminology.model.OauthParameter;
import com.opsnow.terminology.model.Parameter;
import com.opsnow.terminology.model.SheetParameter;
import com.opsnow.terminology.model.Terminology;
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

    @Value("${spring.profiles.active}")
    private String profile;
    @Value("${spring.datasource.password}")
    private String pwd;

    public Map<String,Object> facade(Parameter parameter){


        log.info("{} start",Thread.currentThread().getStackTrace()[1].getMethodName());

        log.info("profile : {}, pwd : {}",profile,pwd);
        Map<String,Object> result = new HashMap<>();
        int code = 200;
        String msg = "success";

        OauthParameter oauthParameter;
        SheetParameter sheetParameter;
        String data;
        JsonArray jsonArray;
        List<Terminology> dataList;



        try{
            oauthParameter = parameter.getOauth_parameter();
            sheetParameter = parameter.getSheet_parameter();
            try{
                String access_token = googleAPI.getAccessTokenByRefreshToken(oauthParameter);
                try{
                    // 1. 데이터를 받아오는 부분
                    sheetParameter.setAccess_token(access_token);
                    data = googleAPI.getSheetDataByToken(sheetParameter);

                    try {
                        // 2. 데이터를 파싱하는 부분 ( 컬럼명과 시트데이터 가져오는 부분 )
                        jsonArray = terminologyService.parseData(data);

                        try{
                            // 3. 가져온 데이터를 VO에 매핑하기 위한 작업들
                            dataList = terminologyService.mappingData(jsonArray);

                            // 4. 데이터 DB에 저장
                            terminologyService.saveData(dataList);

                        } catch (Exception e){
                            code = 503;
                            msg = "데이터 매핑 or db 저장 에러";
                            log.error("{}",msg,e);
                        }
                    } catch (Exception e){
                        code = 502;
                        msg = "데이터 파싱 에러";
                        log.error("{}",msg,e);
                    }
                } catch (Exception e){
                    code = 501;
                    msg = "데이터 받아오기 에러";
                    log.error("{}",msg,e);
                }
            } catch (Exception e){
                code = 401;
                msg = "OAuth 인증 에러";
                log.error("{}",msg,e);
            }
        } catch (Exception e){
            code = 400;
            msg = "파라미터 에러";
            log.error("{}",msg,e);
        } finally {
            log.info(msg);
            log.info("{} end",Thread.currentThread().getStackTrace()[1].getMethodName());
            result.put("code",code);
            result.put("msg",msg);
            return result;
        }

    }
}
