package com.opsnow.terminology;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opsnow.terminology.model.Parameter;
import com.opsnow.terminology.service.Facade;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
public class StreamLambdaHandler implements RequestStreamHandler {

    @NonNull
    private final Facade facade;
    private static Logger logger = LoggerFactory.getLogger(StreamLambdaHandler.class);

    public static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

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

        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }


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
