package com.julius.spring.boot.ethweb3.controller;

import com.julius.spring.boot.ethweb3.service.PropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
public class PropertyController {

    private static final Logger logger = LoggerFactory.getLogger(PropertyController.class);
    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping(path = "/api/property/{propertyId}/value")
    public BigInteger retrievePropertyValue(@PathVariable("propertyId") String propertyId){
        logger.info("retrievePropertyValue");
        return propertyService.getPropertyValue(propertyId);
    }

    @GetMapping(path = "/api/property/{propertyId}/name")
    public String retrievePropertyName(@PathVariable("propertyId") String propertyId){
        logger.info("retrievePropertyName");
        return propertyService.getPropertyName(propertyId);
    }
}
