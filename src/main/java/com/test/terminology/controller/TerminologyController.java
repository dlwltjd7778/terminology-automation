package com.test.terminology.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.terminology.model.Parameter;
import com.test.terminology.service.Facade;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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

        JSONObject result = null;
        JSONParser jsonParser = new JSONParser();

        try {

            Map<String,Object> resultMap = facade.facade(parameter);
            ObjectMapper objectMapper = new ObjectMapper();
            String resultStr = objectMapper.writeValueAsString(resultMap);
            result = (JSONObject) jsonParser.parse(resultStr);

        } catch (Exception e) {
            log.error("",e);
        }

        log.info("{} end",Thread.currentThread().getStackTrace()[1].getMethodName());

        // return 값으로 통신 잘 되었는지 여부 넣기
        return new ResponseEntity(result,HttpStatus.OK);
    }


}
