package com.example.app.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@Service
public class RegisterDevice {
    private static final Logger log = LoggerFactory.getLogger(RegisterDevice.class);
    private final CheckSerialNumber checkSerialNumber;
    private GetSerialNumber getSerialNumber;
    private final DynamoDbClient dynamoDbClient;

    public RegisterDevice(CheckSerialNumber checkSerialNumber, GetSerialNumber getSerialNumber, DynamoDbClient dynamoDbClient){
        this.checkSerialNumber = checkSerialNumber;
        this.getSerialNumber = getSerialNumber;
        this.dynamoDbClient = dynamoDbClient;
    }

    public PutItemResponse registerDevice(Map<String, Object> apiGatewayEvent, String dynamoDbTableName) throws Exception{

        //Get Device Registration Status
        boolean deviceRegistrationStatus = checkSerialNumber.isDeviceRegistered(apiGatewayEvent, dynamoDbTableName);

        //Validate that we have a valid response
        validateNotNullRegistrationStatus(deviceRegistrationStatus);

        //If the device is not registered, we need to register it to DynamoDb
        if (deviceRegistrationStatus == false) {

            //Get the Serial Number
            String serialNumber = getSerialNumber.getSerialNumber(apiGatewayEvent);

            //Get Hash Key to make the request
            Map<String, AttributeValue> keyToGet = getHashKey(serialNumber);

            //Build the Request and get the response
            PutItemResponse dynamoDbResponse = postItemToDynamo(serialNumber, keyToGet, dynamoDbTableName);
            
            return dynamoDbResponse;
        } else {
            log.info("The Raspberry pi with this serial number has already been registered.");
            //Return a response that notifies the user the device was already registered(This is a moc response)
            return PutItemResponse.builder().attributes(Map.of("message",AttributeValue.builder().s("Device already registered!").build())).build();
        }
        
    }

    private void validateNotNullRegistrationStatus(boolean deviceRegistrationStatus){
        log.info("Attempting to verify if the device has a registration status...");
        if(deviceRegistrationStatus == true || deviceRegistrationStatus == false){
            log.info("The Device has a registration Sttaus and will be evaludated");
        } else {
            throw new RuntimeException("Unable to determine if the device is regiustered or not.");
        }
    }

    private Map<String, AttributeValue> getHashKey(String serialNumber){
        log.info("Attempting to build the Hashmap Key for the Serial Number...");
        try{
            Map<String, AttributeValue> keyToGet = new HashMap<>();
            keyToGet.put("serial_number", AttributeValue.builder().s(serialNumber).build());
            log.info("Successfully created the hashmap getToKey for DynamoDB!");
            return keyToGet;
        } catch (RuntimeException e){
            log.error("Error occured while attempting to build the hashmap key to check dynamoDB", e.getMessage(), e);
            throw new RuntimeException();
        }
    };

    private PutItemResponse postItemToDynamo(String serialNumber, Map<String, AttributeValue> item, String dynamoDbTableName){
        log.info("Attempting to post the item to DynamoDB...");
        try{
            PutItemRequest request = PutItemRequest.builder()
                    .tableName(dynamoDbTableName)
                    .item(item)
                    .build();

            PutItemResponse response = dynamoDbClient.putItem(request);
            log.info("Successfully posted the serial number: " + serialNumber + "to Dynamo DB table: " + dynamoDbTableName);
            return response;
        } catch (RuntimeException e){
            log.error("Error occured while attempting to build the putItemRequest or executing the putItemRequest", e.getMessage(),e);
            throw new RuntimeException();
        }
    }
}
