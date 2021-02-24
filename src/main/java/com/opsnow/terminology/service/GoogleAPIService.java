package com.opsnow.terminology.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opsnow.terminology.model.OauthParameter;
import com.opsnow.terminology.model.SheetParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class GoogleAPIService {

    // 리프레쉬 토큰으로 토큰 얻기
    public String getAccessTokenByRefreshToken(OauthParameter parameterVO) throws IOException {

        log.info("start {}",Thread.currentThread().getStackTrace()[1].getMethodName());

//        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//        factory.setConnectTimeout(5000);
//        RestTemplate restTemplate = new RestTemplate(factory);
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://oauth2.googleapis.com/token";

        String client_id = parameterVO.getClient_id();
        String client_secret = parameterVO.getClient_secret();
        String refresh_token = parameterVO.getRefresh_token();
        String grant_type = parameterVO.getGrant_type();

        // body 작성
        Map<String,String> reqBody = new HashMap<>();
        reqBody.put("client_id", client_id);
        reqBody.put("client_secret", client_secret);
        reqBody.put("refresh_token", refresh_token);
        reqBody.put("grant_type",grant_type);

        // Header 작성
        HttpHeaders headers = new HttpHeaders();

        // http entity에 header, body 담아줌
        HttpEntity entity = new HttpEntity<>(reqBody, headers);

        // api 호출 및 값 받아오기
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // response String to Map
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> resMap = null;
        try {
            resMap = mapper.readValue(response.getBody(), Map.class);
        } catch (JsonProcessingException e) {
            log.error("",e);
        }
        String access_token = resMap.get("access_token");

        // check response
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Request Successful {}",Thread.currentThread().getStackTrace()[1].getMethodName());
        } else {
            log.info("Request Failed {}",Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        log.info("end {}",Thread.currentThread().getStackTrace()[1].getMethodName());

        return access_token;

    }



    // 구글 시트에서 토큰으로 데이터 받아오기
    public String getSheetDataByToken(SheetParameter sheetParameter){

        log.info("start {}",Thread.currentThread().getStackTrace()[1].getMethodName());

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://sheets.googleapis.com/v4/spreadsheets/"+ sheetParameter.getSheet_id() +"/values/" + sheetParameter.getSheet_name();

        // Header 작성
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + sheetParameter.getAccess_token());

        // http entity에 header 담아줌
        HttpEntity entity = new HttpEntity<>(headers);

        // api 호출 및 값 받아오기
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            // check response
            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Request Successful {}",Thread.currentThread().getStackTrace()[1].getMethodName());
            } else {
                log.info("Request Failed {}",Thread.currentThread().getStackTrace()[1].getMethodName());
            }
            return response.getBody();
        } catch (Exception e){
            log.error("",e);
            return null;
        } finally {
            log.info("end {}",Thread.currentThread().getStackTrace()[1].getMethodName());
        }

    }

}
