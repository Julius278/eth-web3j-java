package com.julius.spring.boot.ethweb3.service;

import com.julius.spring.boot.ethweb3.Property;
import com.julius.spring.boot.ethweb3.PropertySafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.exceptions.ContractCallException;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.List;

@SuppressWarnings("java:S2629")
@Service
public class ContractService {

    private static final Logger logger = LoggerFactory.getLogger(ContractService.class);

    private final Web3j web3client;
    private final ContractGasProvider gasProvider;
    private final TransactionManager manager;
    private final PropertySafe propertySafe;

    public ContractService(Web3j web3client, ContractGasProvider gasProvider, TransactionManager manager, PropertySafe propertySafe) {
        this.web3client = web3client;
        this.gasProvider = gasProvider;
		this.manager = manager;
		this.propertySafe = propertySafe;
	}

    /**
     *
     * @return contract value
     */
    public BigInteger deployContractAndCallValue() {
        try {
            Property property = Property.deploy(web3client, manager, gasProvider, BigInteger.valueOf(5)).send();
            logger.info("contractAddress: {}", property.getContractAddress());
            TransactionReceipt receipt = property.setValue(BigInteger.valueOf(50)).send();
            logger.info(receipt.getTransactionHash());
            logger.info(String.valueOf(receipt.getBlockNumber()));
            BigInteger value = property.value().send();
            logger.info(String.valueOf(value));

            return value;
        } catch (Exception e) {
            logger.error("failed to deploy Property contract");
            throw new ContractCallException("failed to deploy Property contract, " + e);
        }
    }

    /**
     *
     * @return contract value
     */
    public String deployPropertySafe() {
        try {
            PropertySafe deployedPropertySafe = PropertySafe.deploy(web3client, manager, gasProvider).send();

            return deployedPropertySafe.getContractAddress();
        } catch (Exception e) {
            logger.error("failed to deploy Property contract");
            throw new ContractCallException("failed to deploy Property contract, " + e);
        }
    }

    public String addPropertyToPropertySafe(String propertyId) {
        BigInteger standardValue = BigInteger.valueOf(200L);
		try {
            logger.info("try to create new property by id: {}", propertyId);
			propertySafe.createProperty(propertyId, standardValue).sendAsync().get();
            logger.info("created new property");
            return propertySafe.getPropertyById(propertyId).send();
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
}
