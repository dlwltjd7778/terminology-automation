package com.opsnow.terminology;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.opsnow.terminology.model.Parameter;
import com.opsnow.terminology.service.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;


public class LambdaHandler implements RequestHandler<Map, String> {

    static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            LambdaHandler.class.getPackage().getName());

    public LambdaHandler() {
        context.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Autowired
    Facade facade;

    public String handleRequest(Map input, Context context) {

        Gson gson = new Gson();
        String inputString = gson.toJson(input);
        Parameter parameter = gson.fromJson(inputString, Parameter.class);
        String result = facade.facade(parameter).toString();

        return result;
    }
}