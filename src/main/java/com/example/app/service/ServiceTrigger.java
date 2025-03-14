package com.example.app.service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

@Service
public class ServiceTrigger {
    private static final Logger log = LoggerFactory.getLogger(ServiceTrigger.class);

    private final RegisterDevice registerDevice;

    public ServiceTrigger(RegisterDevice registerDevice){
        this.registerDevice = registerDevice;
    }

    @Value("${aws.databases.dynamodb.serialnumbers}")
    private String dynamoDbTableName;

    @Value("${spring.profiles.active}")
    private String environment;


    public PutItemResponse TriggerService(Map<String, Object> apiGatewayEvent) throws Exception{
        //Initialization Logs
        log.info("Begining to kick off the task 1 lambda function...");
        log.info("The Active Environment is set to: " + environment);
        PutItemResponse dynamoDbResponse = registerDevice.registerDevice(apiGatewayEvent, dynamoDbTableName);
        
        return dynamoDbResponse;
    }
}