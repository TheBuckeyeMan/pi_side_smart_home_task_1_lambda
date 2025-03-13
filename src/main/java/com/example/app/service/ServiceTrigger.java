package com.example.app.service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServiceTrigger {
    private static final Logger log = LoggerFactory.getLogger(ServiceTrigger.class);

    private final RegisterDevice registerDevice;

    public ServiceTrigger(RegisterDevice registerDevice){
        this.registerDevice = registerDevice;
    }


    @Value("${spring.profiles.active}")
    private String environment;


    public void TriggerService(){
        //Initialization Logs
        log.info("Begining to kick off the task 1 lambda function...");
        log.info("The Active Environment is set to: " + environment);
        registerDevice.registerDevice(environment);
        

    }
}