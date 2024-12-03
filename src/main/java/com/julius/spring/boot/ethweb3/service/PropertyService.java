package com.julius.spring.boot.ethweb3.service;

import com.julius.spring.boot.ethweb3.Property;
import com.julius.spring.boot.ethweb3.PropertySafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;

@SuppressWarnings({"java:S2629", "java:S112"})
@Service
public class PropertyService {

    private static final Logger logger = LoggerFactory.getLogger(PropertyService.class);

    private final Web3j web3client;
    private final ContractGasProvider gasProvider;
    private final PropertySafe propertySafe;
	private final Credentials credentials;

	public PropertyService(Web3j web3client, ContractGasProvider gasProvider, PropertySafe propertySafe, Credentials credentials) {
        this.web3client = web3client;
        this.gasProvider = gasProvider;
		this.propertySafe = propertySafe;
		this.credentials = credentials;
	}

	public BigInteger getPropertyValue(String propertyId) {
		try {
			logger.info("get property value for propertyId: {}", propertyId);
			return retrieveProperty(propertyId).getValue().send();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getPropertyName(String propertyId) {
		try {
			logger.info("get property name for propertyId: {}", propertyId);
			return retrieveProperty(propertyId).getName().send();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getPropertyDescription(String propertyId) {
		try {
			logger.info("get property description for propertyId: {}", propertyId);
			return retrieveProperty(propertyId).getDescription().send();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setPropertyDescription(String propertyId, String description) {
		try {
			logger.info("set property description for propertyId: {}", propertyId);
			retrieveProperty(propertyId).setDescription(description).send();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public BigInteger useSumFunction(String propertyId, int a, int b) {
		try {
			logger.info("use sum function with property '{}', int a: '{}', int b: '{}'", propertyId, a, b);
			return retrieveProperty(propertyId).sum(BigInteger.valueOf(a), BigInteger.valueOf(b)).send();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

    private Property retrieveProperty(String propertyId) {
		try {
			logger.info("retrieve property by propertyId: {}", propertyId);
			var propertyAddress = propertySafe.getPropertyById(propertyId).send();
            return Property.load(propertyAddress, web3client, credentials, gasProvider);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
