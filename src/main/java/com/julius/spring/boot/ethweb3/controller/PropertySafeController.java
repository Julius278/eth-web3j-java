package com.julius.spring.boot.ethweb3.controller;

import com.julius.spring.boot.ethweb3.model.PropertyModel;
import com.julius.spring.boot.ethweb3.service.PropertySafeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
public class PropertySafeController {

    private static final Logger logger = LoggerFactory.getLogger(PropertySafeController.class);

    private final PropertySafeService propertySafeService;

    public PropertySafeController(PropertySafeService propertySafeService) {
        this.propertySafeService = propertySafeService;
    }

    @GetMapping(path = "/api/propertySafe/deploy")
    public String deploySafe(){
        logger.info("deploySafe");
        return propertySafeService.deployPropertySafe();
    }

    @PostMapping(path = "/api/propertySafe/property/{propertyId}")
    public BigInteger addPropertyToPropertySafe(@PathVariable("propertyId") String propertyId, @RequestParam("propertyName") String propertyName,@RequestParam("propertyValue") int propertyValue){
        logger.info("addPropertyToPropertySafe");
        return propertySafeService.deployPropertyToSafe(propertyId, propertyName, propertyValue);
    }

    @GetMapping(path = "/api/propertySafe/properties")
    public List<PropertyModel> listPropertySafe(){
        logger.info("listPropertySafe");
        return propertySafeService.listPropertySafe();
    }
}
