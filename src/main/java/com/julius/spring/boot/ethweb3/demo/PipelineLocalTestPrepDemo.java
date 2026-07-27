package com.julius.spring.boot.ethweb3.demo;

import com.julius.spring.boot.ethweb3.Property;
import com.julius.spring.boot.ethweb3.PropertySafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.crypto.exception.CipherException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.math.BigInteger;

@SuppressWarnings("java:S112")
public class PipelineLocalTestPrepDemo {

	public static final Logger LOGGER = LoggerFactory.getLogger(PipelineLocalTestPrepDemo.class);
	public static final String KEY_FILE_PATH = "./src/main/resources/keyfile.json";
	public static final String ETH_SERVER_ADDRESS = "http://localhost:8545";

	public static final String EXTERNAL_PROPERTY_ID = "dummy-external-property-id";
	private static final String PROPERTY_NAME = "dummy-property-name";
	public static final int PROPERTY_VALUE = 100;


	public static void main(String[] args) throws Exception {

		// setup for general node query
		final Web3j web3jConnection = web3jClient();
		LOGGER.info("connected node: {}", web3jConnection.web3ClientVersion().send().getWeb3ClientVersion());
		LOGGER.info("current block number: {}", web3jConnection.ethBlockNumber().send().getBlockNumber());

		// setup for sending transactions
		final Credentials credentials = loadCredentials(KEY_FILE_PATH);
		final TransactionManager manager = createTransactionManager(web3jConnection, credentials);
		final ContractGasProvider gasProvider = new DefaultGasProvider();

		// load the PropertySafe with the given address (pre-deployed)
		PropertySafe propertySafe = PropertySafe.deploy( web3jConnection, manager, gasProvider).send();
		LOGGER.info("property deployed on address: {}", propertySafe.getContractAddress());
		LOGGER.info("deployPropertyToSafe for id: {} on propertySafe: {}", EXTERNAL_PROPERTY_ID, propertySafe.getContractAddress());

		// deploy a new Property and add it to the PropertySafe
		Property property = Property.deploy(web3jConnection, manager, gasProvider, PROPERTY_NAME, BigInteger.valueOf(PROPERTY_VALUE)).send();
		property.setPropertyId(EXTERNAL_PROPERTY_ID).send();
		LOGGER.info("contractAddress of deployed property: {}", property.getContractAddress());
		propertySafe.addProperty(EXTERNAL_PROPERTY_ID, property.getContractAddress()).send();
		LOGGER.info("successfully added property to the PropertySafe");
	}

	public static Credentials loadCredentials(String keyFilePath) {
		Credentials credentials = null;
		try {
			LOGGER.info("loading key file: {}", keyFilePath);
			credentials = WalletUtils.loadCredentials("password", keyFilePath);
			LOGGER.info("successfully loaded credentials, address: {}", credentials.getAddress());
		} catch (IOException | CipherException e) {
			LOGGER.error("Failed to load credentials", e);
		}
		return credentials;
	}

	public static Web3j web3jClient() {
		LOGGER.info("ethereum test node: {}", ETH_SERVER_ADDRESS);
		return Web3j.build(new HttpService(ETH_SERVER_ADDRESS));
	}

	public static TransactionManager createTransactionManager(Web3j web3client, Credentials credentials) {
		try {
			return new RawTransactionManager(web3client, credentials, Long.parseLong(web3client.netVersion().send().getNetVersion()));
		} catch (IOException e) {
			LOGGER.error("transactionManager initiate failed", e);
			throw new RuntimeException("could not initiate transactionManager");
		}
	}
}
