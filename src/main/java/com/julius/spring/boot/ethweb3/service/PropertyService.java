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
import java.util.List;

@SuppressWarnings("java:S2629")
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

    public String addPropertyToPropertySafe(String propertyId) {
        BigInteger standardValue = BigInteger.valueOf(200L);
		try {
            logger.info("try to create new property by id: {}", propertyId);
			propertySafe.createProperty(propertyId, standardValue).send();
            logger.info("created new property");
            return propertySafe.getPropertyById(propertyId).send();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public BigInteger getPropertyValue(String propertyId) {
		try {
			return retrieveProperty(propertyId).getValue().send();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

    public List getProperties() {
        try {
            logger.info("try to get a list of properties");
            return propertySafe.getProperties().sendAsync().get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Property retrieveProperty(String propertyId) {
		try {
			var propertyAddress = propertySafe.getPropertyById(propertyId).send();
            return Property.load(propertyAddress, web3client, credentials, gasProvider);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
