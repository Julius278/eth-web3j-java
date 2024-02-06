package com.julius.spring.boot.ethweb3.service;

import com.julius.spring.boot.ethweb3.config.Web3Config;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.http.HttpService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class Web3Service {

    private final Web3j web3c;

    public Web3Service(Web3Config web3Config){
        web3c = Web3j.build(new HttpService(web3Config.getEthServerAddress()));
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
}
