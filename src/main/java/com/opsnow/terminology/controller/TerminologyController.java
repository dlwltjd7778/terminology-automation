package com.opsnow.terminology.controller;

import com.opsnow.terminology.model.Parameter;
import com.opsnow.terminology.service.Facade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TerminologyController {

    private final Facade facade;

    @PostMapping ("/test")
    public ResponseEntity test(@RequestBody Parameter parameter){

        System.out.println("METHOD START - test in TerminologyController!");

        System.out.println("parameter:" + parameter);

        Map<String,Object> result = facade.facade(parameter);

        System.out.println("METHOD END - test in TerminologyController");

        // return 값으로 통신 잘 되었는지 여부 넣기

        return new ResponseEntity(result,HttpStatus.OK);
    }





    // 스크립트 앱스 코드 짜보다 실패함
//    @GetMapping(path= "/test2", produces = "application/json")
//    public @ResponseBody
//    JsonObject test2(){
//        RestTemplate restTemplate = new RestTemplate();
//
//
//        String url = "https://script.google.com/a/macros/bespinglobal.com/s/AKfycbyahNz6wqVBRTJ6cPe6OUzXv7OpAI1SNTl0CpBihZMtCG-D5hKZYb5R/exec?sheetName=Dictionary";
//
//        // Header 작성
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);   //Response Header to UTF-8
//
//        // http entity에 header 담아줌
//        HttpEntity entity = new HttpEntity<>(headers);
//
//        // api 호출 및 값 받아오기
//
//        ResponseEntity<JsonObject> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonObject.class);
//        // check response
//        if (response.getStatusCode() == HttpStatus.OK) {
//            System.out.println("Request Successful(getSheetData)");
//        } else {
//            System.out.println("Request Failed(getSheetData)");
//        }
//        return response.getBody();
//
//    }

}
