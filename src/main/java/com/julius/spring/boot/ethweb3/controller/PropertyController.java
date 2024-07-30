package com.julius.spring.boot.ethweb3.controller;

import com.julius.spring.boot.ethweb3.service.ContractService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PropertyController {

    private final ContractService contractService;

    public PropertyController(ContractService contractService) {
        this.contractService = contractService;
    }

    @SuppressWarnings("java:S3740")
    @GetMapping(
        path = "/api/propertySafe/properties",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public List fetchProperties(){
        return contractService.getProperties();
    }
}
