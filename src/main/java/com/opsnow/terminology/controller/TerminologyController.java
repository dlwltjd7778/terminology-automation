package com.opsnow.terminology.controller;

import com.opsnow.terminology.model.Parameter;
import com.opsnow.terminology.service.Facade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TerminologyController {

    private final Facade facade = new Facade();

    @PostMapping ("/test")
    public ResponseEntity test(@RequestBody Parameter parameter){

        System.out.println("METHOD START - test in TerminologyController!");

        Map<String,Object> result = facade.facade(parameter);

        System.out.println("METHOD END - test in TerminologyController");

        // return 값으로 통신 잘 되었는지 여부 넣기
        return new ResponseEntity(result,HttpStatus.OK);
    }

}
