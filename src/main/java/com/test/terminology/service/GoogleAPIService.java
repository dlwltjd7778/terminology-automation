package com.test.terminology.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.terminology.model.OauthParameter;
import com.test.terminology.model.SheetParameter;
import com.test.terminology.util.ResultCode;
import com.test.terminology.util.MyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class GoogleAPIService {

    @Value("${OAUTH_REQ_URL}")
    String OAUTH_REQ_URL;
    @Value("${SHEET_REQ_URL}")
    String SHEET_REQ_URL;

    /*
         method :       getAccessTokenByRefreshToken
         Parameter :    OauthParameter
         Return :       String (access_token)
         desc :         리프레쉬 토큰으로 토큰 얻기
     */
    public String getAccessTokenByRefreshToken(OauthParameter oauthParameter) throws MyException {

        log.info("start {}",Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            RestTemplate restTemplate = new RestTemplate();

            String client_id = oauthParameter.getClient_id();
            String client_secret = oauthParameter.getClient_secret();
            String refresh_token = oauthParameter.getRefresh_token();
            String grant_type = oauthParameter.getGrant_type();

            // body 작성
            Map<String, String> reqBody = new HashMap<>();
            reqBody.put("client_id", client_id);
            reqBody.put("client_secret", client_secret);
            reqBody.put("refresh_token", refresh_token);
            reqBody.put("grant_type", grant_type);

            // http entity에 body 담아줌
            HttpEntity entity = new HttpEntity<>(reqBody);

            // api 호출 및 값 받아오기
            ResponseEntity<String> response = restTemplate.exchange(OAUTH_REQ_URL, HttpMethod.POST, entity, String.class);

            // response String to Map
            ObjectMapper mapper = new ObjectMapper();

            // Response 를 맵에 매핑
            Map<String, String> resMap = mapper.readValue(response.getBody(), Map.class); // throws JsonProcessingException

            // 매핑된 값에서 access_token 값만 가져옴
            String access_token = resMap.get("access_token");

            log.info("end {}",Thread.currentThread().getStackTrace()[1].getMethodName());
            return access_token;

        } catch (Exception e){
            throw new MyException(ResultCode.OAuthAPIException,e.initCause(e.getCause()));
        }
    }


    /*
         method :       getSheetDataByToken
         Parameter :    SheetParameter
         Return :       String (엑셀 시트 데이터)
         desc :         구글 시트에서 토큰으로 데이터 받아오기
     */
    public String getSheetDataByToken(SheetParameter sheetParameter) throws MyException {

        log.info("start {}",Thread.currentThread().getStackTrace()[1].getMethodName());

        try{

            RestTemplate restTemplate = new RestTemplate();

            String url = String.format(SHEET_REQ_URL,sheetParameter.getSheet_id(),sheetParameter.getSheet_name());

            // Header 작성
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + sheetParameter.getAccess_token());

            // http entity에 header 담아줌
            HttpEntity entity = new HttpEntity<>(headers);

            // api 호출 및 값 받아오기
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            log.info("end {}",Thread.currentThread().getStackTrace()[1].getMethodName());

            return response.getBody();

        } catch (Exception e){
            throw new MyException(ResultCode.SheetAPIException,e.initCause(e.getCause()));
        }
    }
}
