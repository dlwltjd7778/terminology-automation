package com.opsnow.terminology;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opsnow.terminology.model.Parameter;
import com.opsnow.terminology.service.Facade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class StreamLambdaHandler implements RequestStreamHandler {

    //private final Facade facade = new Facade();
    private static Logger logger = LoggerFactory.getLogger(StreamLambdaHandler.class);

//    @Autowired
//    DBConfig dbConfig;

    public static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
  //  private static final ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationContext.class);

    static AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
            StreamLambdaHandler.class.getPackage().getName());

    public StreamLambdaHandler() {
        ctx.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Autowired
    private Facade facade;

    static {
        try {
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(TerminologyApplication.class);

//            handler.onStartup(servletContext -> {
//                FilterRegistration.Dynamic registration = servletContext.addFilter("CognitoIdentityFilter", CognitoIdentityFilter.class);
//                registration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
//            });
        } catch (ContainerInitializationException e) {
            // if we fail here. We re-throw the exception to force another cold start
            String errMsg = "Could not initialize Spring Boot application";
            e.printStackTrace();
            logger.error(errMsg);
            System.out.println(errMsg);
            throw new RuntimeException("Could not initialize Spring Boot application", e);
        }
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        LambdaLogger logger = context.getLogger();
        logger.log("------------handleRequest Start------------₩n");

       // handler.proxyStream(inputStream, outputStream, context);

        // inputStream to String
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }

       logger.log("-----springContext===> {}" + context );
//        DBConfig dbConfig = springContext.getBean(DBConfig.class);
//        System.out.println("url???/???" + dbConfig.getUrl());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String parameterString = textBuilder.toString();

        Parameter parameter = gson.fromJson(parameterString,Parameter.class);

        System.out.println("METHOD START - test in handler!");

        System.out.println("parameter:" + parameter);

        Map<String,Object> result = facade.facade(parameter);

        String resultString = result.toString();

        System.out.println("METHOD END - test in handler");


        // just in case it wasn't closed
        System.out.println("---inputStream---" + textBuilder.toString());
        System.out.println("---outputStream---" + resultString);
        outputStream.close();
    }






}
