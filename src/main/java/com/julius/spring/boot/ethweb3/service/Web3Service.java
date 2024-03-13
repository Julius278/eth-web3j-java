package com.julius.spring.boot.ethweb3.service;

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
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
public class Web3Service {

    private final Web3j web3c;

    public Web3Service(Web3j web3c) {
        this.web3c = web3c;
    }

    public EthBlockNumber getEthBlockNumber() {
        EthBlockNumber result;
        try {
            result = web3c.ethBlockNumber()
                    .sendAsync()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public EthAccounts getEthAccounts() {
        EthAccounts result;
        try {
            result = web3c.ethAccounts()
                    .sendAsync()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public EthGetBalance getEthBalance(String address) {
        EthGetBalance result;
        try {
            result = web3c.ethGetBalance(address,
                            DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public EthGetTransactionCount getTransactionCount(String address) {
        EthGetTransactionCount result;
        try {
            result = web3c.ethGetTransactionCount(address,
                            DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public String sendTransaction(TransferRequest transferRequest) {
        TransactionReceipt transactionReceipt;
        try {
            Future<TransactionReceipt> future = Transfer.sendFundsEIP1559(
                    web3c,
                    Credentials.create(transferRequest.getPrivateKey()),
                    transferRequest.getRecipient(),
                    transferRequest.getAmountInEth(),
                    Convert.Unit.ETHER,
                    BigInteger.valueOf(8_000_000L), //gaslimit
                    DefaultGasProvider.GAS_LIMIT,
                    BigInteger.valueOf(3_100_000_000L) //maxFeePerGas
            ).sendAsync();
            System.out.print("transaction sent..");
            while(!future.isDone()){
                Thread.sleep(1500);
                System.out.print(".");
            }
            transactionReceipt = future.get();
            System.out.println("\nsending eth ended");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return transactionReceipt.getTransactionHash();
    }
}
