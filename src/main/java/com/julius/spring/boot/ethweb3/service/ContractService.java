package com.julius.spring.boot.ethweb3.service;

import com.julius.spring.boot.ethweb3.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.exceptions.ContractCallException;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;

@SuppressWarnings("java:S2629")
@Service
public class ContractService {

    private static final Logger logger = LoggerFactory.getLogger(ContractService.class);

    private final Web3j web3client;
    private final Credentials credentials;
    private final ContractGasProvider gasProvider;

    public ContractService(Web3j web3client, Credentials credentials, ContractGasProvider gasProvider) {
        this.web3client = web3client;
        this.credentials = credentials;
        this.gasProvider = gasProvider;
    }

    /**
     *
     * @return contract value
     */
    public BigInteger deployContractAndCallValue() {
        try {
            logger.info("own address: {}", credentials.getAddress());
            TransactionManager manager = new RawTransactionManager(web3client, credentials, Long.parseLong(web3client.netVersion().send().getNetVersion()));
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
}
