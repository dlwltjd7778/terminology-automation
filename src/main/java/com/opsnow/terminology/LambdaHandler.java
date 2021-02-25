package com.opsnow.terminology;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.opsnow.terminology.model.Parameter;
import com.opsnow.terminology.service.Facade;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Profile;

import java.util.Map;

@Slf4j
public class LambdaHandler implements RequestHandler<Map, String> {

    // bean 주입
    static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            LambdaHandler.class.getPackage().getName());

    public LambdaHandler() {
        context.getAutowireCapableBeanFactory().autowireBean(this);
    }

//    private static SpringBootLambdaContainerHandler handler;
//    static{
//        try{
//            String SPRING_PROFILES_ACTIVE = System.getenv("SPRING_PROFILES_ACTIVE");
//            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(TerminologyApplication.class);
//            //handler.activateSpringProfiles(SPRING_PROFILES_ACTIVE);
//            handler.initialize();
//
//            System.out.println("환경변수????? >>>>>>" + System.getenv("SPRING_PROFILES_ACTIVE"));
//            log.info("여기 찍히나...?");
//        } catch (ContainerInitializationException e){
//            e.printStackTrace();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }






    @Autowired
    Facade facade;

    public String handleRequest(Map input, Context context) {

        log.info("start {}",Thread.currentThread().getStackTrace()[1].getMethodName());

        Gson gson = new Gson();
        JSONParser jsonParser = new JSONParser();

        // input ( map to object )
        String inputString = gson.toJson(input);
        Parameter parameter = gson.fromJson(inputString, Parameter.class);

        // output ( map to json )
        Map<String,Object> resultMap = facade.facade(parameter);
        String resultStr = gson.toJson(resultMap);

        //JsonObject result = (JSONObject) jsonParser.parse(resultStr);

        log.info("end {}",Thread.currentThread().getStackTrace()[1].getMethodName());
        return resultStr;
    }
}