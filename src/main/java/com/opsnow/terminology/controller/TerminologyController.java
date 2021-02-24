package com.opsnow.terminology.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opsnow.terminology.model.Parameter;
import com.opsnow.terminology.service.Facade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class TerminologyController {

    @Autowired
    private Facade facade;

    @PostMapping ("/test")
    public ResponseEntity test(@RequestBody Parameter parameter){

        log.info("{} start",Thread.currentThread().getStackTrace()[1].getMethodName());

        Map<String,Object> result = facade.facade(parameter);

        log.info("{} end",Thread.currentThread().getStackTrace()[1].getMethodName());

        // return 값으로 통신 잘 되었는지 여부 넣기
        return new ResponseEntity(result,HttpStatus.OK);
    }

    @PostMapping ("/testjson")
    public JsonObject testjson(@RequestBody Parameter parameter){

        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();

        // output ( map to json )
        Map<String,Object> resultMap = facade.facade(parameter);
        String resultStr = gson.toJson(resultMap);
        System.out.println("map to string >>> " + resultStr);
        JsonObject result = (JsonObject) jsonParser.parse(resultStr);


        return result;
    }

}
