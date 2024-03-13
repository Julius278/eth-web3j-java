package com.julius.spring.boot.ethweb3.service;

import com.julius.spring.boot.ethweb3.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.exceptions.ContractCallException;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

@SuppressWarnings("java:S2629")
@Service
public class ContractService {

    private static final Logger logger = LoggerFactory.getLogger(ContractService.class);

    private final Web3j web3client;
    private final Credentials credentials;
    private final StaticGasProvider gasProvider;

    public ContractService(Web3j web3client, Credentials credentials, StaticGasProvider gasProvider) {
        this.web3client = web3client;
        this.credentials = credentials;
        this.gasProvider = gasProvider;
    }

    /**
     * TODO FIXME: JsonRpcError thrown with code -32000. Message: only replay-protected (EIP-155) transactions allowed over RPC
     *
     * @return contract value
     */
    public BigInteger deployContractAndCallValue() {
        try {
            Property property = Property.deploy(web3client, credentials, gasProvider).send();
            logger.info(property.getContractAddress());
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
