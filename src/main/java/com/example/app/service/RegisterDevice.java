package com.example.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RegisterDevice {
    private static final Logger log = LoggerFactory.getLogger(RegisterDevice.class);
    private final CheckSerialNumber checkSerialNumber;

    public RegisterDevice(CheckSerialNumber checkSerialNumber){
        this.checkSerialNumber = checkSerialNumber;
    }

    public void registerDevice(String serialNumber){

        //Validate the Serial Number
        boolean deviceRegistrationStatus = checkSerialNumber.isDeviceRegistered(serialNumber);


    }


    

}
