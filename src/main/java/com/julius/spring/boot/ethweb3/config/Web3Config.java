package com.julius.spring.boot.ethweb3.config;

import com.julius.spring.boot.ethweb3.PropertySafe;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.crypto.exception.CipherException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("java:S125")
@Getter
@Configuration
public class Web3Config {

    private static final Logger logger = LoggerFactory.getLogger(Web3Config.class);

    @Value("${eth.node.address}")
    private String ethServerAddress;

    @Value("${web3.keyfile}")
    private String keyFile;

    @Value("${web3.keyfile.password}")
    private String keyFilePassword;

    @Value("${web3.propertySafe.address}")
    private String propertySafeAddress;


    @Bean
    public ContractGasProvider staticGasProvider() {
        return new DefaultGasProvider();
    }

    @Bean
    public Web3j web3jClient() {
        logger.info("eth node = {}", ethServerAddress);
        return Web3j.build(new HttpService(ethServerAddress));
    }

    @Bean
    public Credentials credentials() {
        Credentials credentials = null;
        try {
            File key = ResourceUtils.getFile(keyFile);
            logger.info("keyfile path: {}", key.getPath());
            credentials = WalletUtils.loadCredentials(keyFilePassword, key);
            logger.info("successfully created credentials, address: {}", credentials.getAddress());
        } catch (IOException | CipherException e) {
            logger.error("Failed to load credentials", e);
        }
        return credentials;
    }

    @Bean
    public TransactionManager transactionManager(Web3j web3client, Credentials credentials) {
		try {
			return new RawTransactionManager(web3client, credentials, Long.parseLong(web3client.netVersion().send().getNetVersion()));
		} catch (IOException e) {
            logger.error("transactionManager initiate failed", e);
			throw new RuntimeException("could not initiate transactionManager");
		}
	}

    @Bean
    public PropertySafe propertySafe(Web3j web3j, TransactionManager transactionManager, ContractGasProvider gasProvider){
        logger.info("load propertySafe from address: {}", propertySafeAddress);
        return PropertySafe.load(propertySafeAddress, web3j, transactionManager, gasProvider);
    }

}
