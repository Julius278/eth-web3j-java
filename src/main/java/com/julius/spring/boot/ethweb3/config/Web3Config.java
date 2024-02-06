package com.julius.spring.boot.ethweb3.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class Web3Config {

    @Value("${eth.node.address}")
    private String ethServerAddress;

}
