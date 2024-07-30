package com.julius.spring.boot.ethweb3.controller;

import com.julius.spring.boot.ethweb3.service.PropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
public class PropertyController {

    private static final Logger logger = LoggerFactory.getLogger(PropertyController.class);
    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @SuppressWarnings("java:S3740")
    @GetMapping(
        path = "/api/propertySafe/properties",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public List fetchProperties(){
        return propertyService.getProperties();
    }

    @GetMapping(path = "/api/property/{propertyId}/value")
    public BigInteger retrievePropertyValue(@PathVariable("propertyId") String propertyId){
        logger.info("deploySafe");
        return propertyService.getPropertyValue(propertyId);
    }
}
