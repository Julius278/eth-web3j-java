package com.julius.spring.boot.ethweb3.service;

import com.julius.spring.boot.ethweb3.model.TransferRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.tx.exceptions.ContractCallException;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.util.concurrent.Future;

import static java.lang.String.format;

@SuppressWarnings("java:S106")
@Service
public class Web3Service {

    private static final Logger logger = LoggerFactory.getLogger(Web3Service.class);

    private final Web3j web3c;
    private final Credentials credentials;

    public Web3Service(Web3j web3c, Credentials credentials) {
        this.web3c = web3c;
		this.credentials = credentials;
	}

    public EthBlockNumber getEthBlockNumber() {
        EthBlockNumber result;
        try {
            result = web3c.ethBlockNumber().send();
        } catch (IOException e) {
            logger.error("could not get eth block number: {}", e.getMessage());
            throw new ContractCallException("could not get eth block number");
        }
        return result;
    }

    public EthAccounts getEthAccounts() {
        EthAccounts result;
        try {
            result = web3c.ethAccounts().send();
        } catch (IOException e) {
            logger.error("could not get eth accounts: {}", e.getMessage());
            throw new ContractCallException("could not get eth accounts");        }
        return result;
    }

    public EthGetBalance getEthBalance(String address) {
        EthGetBalance result;
        try {
            result = web3c.ethGetBalance(address,
                            DefaultBlockParameterName.LATEST)
                    .send();
        } catch (IOException e) {
            logger.error("could not get eth balance: {}", e.getMessage());
            throw new ContractCallException(format("could not get eth balance for address %s", address));        }
        return result;
    }

    public EthGetTransactionCount getTransactionCount(String address) {
        EthGetTransactionCount result;
        try {
            result = web3c.ethGetTransactionCount(address,
                            DefaultBlockParameterName.LATEST).send();
        } catch (IOException e) {
            logger.error("could not get transaction count: {}", e.getMessage());
            throw new ContractCallException("could not get transaction count");
        }
        return result;
    }

    public String sendTransaction(TransferRequest transferRequest) {
        TransactionReceipt transactionReceipt;
        try {
            Future<TransactionReceipt> future = Transfer.sendFundsEIP1559(
                    web3c,
                    credentials,
                    transferRequest.getRecipient(),
                    transferRequest.getAmountInEth(),
                    Convert.Unit.ETHER,
                    //BigInteger.valueOf(8_000_000L), //gaslimit
                    DefaultGasProvider.GAS_LIMIT,
                    DefaultGasProvider.GAS_LIMIT,
                    //BigInteger.valueOf(3_100_000_000L) //maxFeePerGas
                    DefaultGasProvider.GAS_PRICE
            ).sendAsync();

            System.out.print("transaction sent..");
            while(!future.isDone()){
                Thread.sleep(1500);
                System.out.print(".");
            }
            transactionReceipt = future.get();
            logger.info("sending eth ended");
        } catch (InterruptedException e) {
            logger.error("could not send transaction, {}", e.getMessage());
            Thread.currentThread().interrupt();
            throw new ContractCallException("could not send transaction");
        } catch (Exception e) {
            logger.error("could not send transaction, {}", e.getMessage());
            throw new ContractCallException("could not send transaction");
        }
		return transactionReceipt.getTransactionHash();
    }
}
