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

    @GetMapping(path = "/api/property/{propertyId}/description")
    public String retrievePropertyDescription(@PathVariable("propertyId") String propertyId){
        logger.info("retrievePropertyDescription");
        return propertyService.getPropertyDescription(propertyId);
    }

    @PostMapping(path = "/api/property/{propertyId}/description")
    public String setPropertyDescription(@PathVariable("propertyId") String propertyId, @RequestParam("description") String description){
        logger.info("setPropertyDescription");
        propertyService.setPropertyDescription(propertyId, description);
        return "successfully set description for property " + propertyId;
    }
}
