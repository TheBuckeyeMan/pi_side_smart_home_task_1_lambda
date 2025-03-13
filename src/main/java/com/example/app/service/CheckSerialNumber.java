package com.example.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.app.config.DynamoConfig;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Service
public class CheckSerialNumber {
    private static final Logger log = LoggerFactory.getLogger(CheckSerialNumber.class);
    private final DynamoDbClient dynamoDbClient;

    @Value("${aws.databases.dynamodb.serialnumbers}")
    private String dynamoDbTableName;


    public CheckSerialNumber(DynamoDbClient dynamoDbClient){
        this.dynamoDbClient = dynamoDbClient;
    }

    public boolean isDeviceRegistered(String serialNumber){
        //Check that we have a serial number
        validateSerialNumberExists(serialNumber);

        //

        return false;
    }

    private void validateSerialNumberExists(String serialNumber){
        log.info("Attempting to verify Serial Number Exists...");
        try{
            if (serialNumber == "" || serialNumber.isEmpty()){
                log.info("Serial Number: " + serialNumber + " was passed successfully to the lambda function!");
            } else {
                log.error("No Serial Number passsed to Lambda...");
                throw new IllegalArgumentException("The Value Passed to the lambda number for the serial number was empty or null...");
            }
        } catch (RuntimeException e){
            log.error("Unable to check if the Serial Number was passed to lambda or not: CheckSerialNumber.java Line 29");
            log.error("The Error Message is: " + e.getMessage());
            log.error("The Error was: " + e);
        }
    }
}
