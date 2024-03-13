package com.julius.spring.boot.ethweb3.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

@Getter
@Configuration
public class Web3Config {

    @Value("${eth.node.address}")
    private String ethServerAddress;

    @Bean
    public StaticGasProvider staticGasProvider() {
        return new StaticGasProvider(BigInteger.valueOf(1L), BigInteger.valueOf(100_000_000_000L));
    }

    @Bean
    public Web3j web3jClient() {
        return Web3j.build(new HttpService(ethServerAddress));
    }

}
