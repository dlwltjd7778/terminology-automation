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

    /*
        - AnnotationConfigApplicationContext : @Configuration 어노테이션이 붙은 클래스를 설정 정보로 사용한다
                                               파라미터에 패키지를 넣으면 그 패키지를 스캔해준다.
        - LambdaHandler Class 가 실행될 때 bean 을 수동으로 주입
        - AutowireCapableBeanFactory : 의존 관계를 만들어주면서 bean 을 주입해준다.

        Spring에서는 클래스 경로 스캔을 사용하여 빈을 자동 감지하고 자동 구성하는 데 사용되는 @Component 클래스를 만든다.
        그런 다음 컨테이너는 빈을 생성하고 주입한다.
        AWS Lambda의 경우 'Lambda 함수 핸들러 객체'가 이미 만들어져 버린다.
        AWS에서 생성한 핸들러 객체는 해당 객체에 대한 종속성 주입 을 허용하도록 초기화되는 동안 ApplicationContext에 의해 빈으로 연결된다.
     */
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