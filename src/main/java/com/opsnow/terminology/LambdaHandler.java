package com.opsnow.terminology;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.opsnow.terminology.model.Parameter;
import com.opsnow.terminology.service.Facade;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

@Slf4j
public class LambdaHandler implements RequestHandler<Map, JSONObject> {

    // bean 주입
    static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            LambdaHandler.class.getPackage().getName());

    public LambdaHandler() {
        context.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Autowired
    Facade facadeService;

    // handler
    public JSONObject handleRequest(Map input, Context context) {

        log.info("start {}",Thread.currentThread().getStackTrace()[1].getMethodName());

        Gson gson = new Gson();
        JSONParser jsonParser = new JSONParser();
        String resultStr = null;
        JSONObject result = null;

        // input ( map to object )
        String inputString = gson.toJson(input);
        Parameter parameter = gson.fromJson(inputString, Parameter.class);

        // 로직 실행
        Map<String,Object> resultMap = facadeService.facade(parameter);

        // output ( map to json )
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            resultStr = objectMapper.writeValueAsString(resultMap);
            result = (JSONObject) jsonParser.parse(resultStr);
        } catch (JsonProcessingException e) {
            log.error("",e);
        } catch (ParseException e) {
            log.error("",e);
        }

        log.info("end {}",Thread.currentThread().getStackTrace()[1].getMethodName());
        return result;
    }
}