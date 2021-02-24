package com.opsnow.terminology;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opsnow.terminology.model.Parameter;
import com.opsnow.terminology.service.Facade;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

@Slf4j
public class LambdaHandler implements RequestHandler<Map, String> {

    // bean 주입
    static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            LambdaHandler.class.getPackage().getName());

    public LambdaHandler() {
        context.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Autowired
    Facade facade;

    public String handleRequest(Map input, Context context) {

        log.info("{} start",Thread.currentThread().getStackTrace()[1].getMethodName());

        Gson gson = new Gson();
        JSONParser jsonParser = new JSONParser();

        // input ( map to object )
        String inputString = gson.toJson(input);
        Parameter parameter = gson.fromJson(inputString, Parameter.class);

        // output ( map to json )
        Map<String,Object> resultMap = facade.facade(parameter);
        String resultStr = gson.toJson(resultMap);

        //JsonObject result = (JSONObject) jsonParser.parse(resultStr);


        log.info("{} end",Thread.currentThread().getStackTrace()[1].getMethodName());
        return resultStr;
    }
}