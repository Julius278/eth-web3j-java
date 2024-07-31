package com.julius.spring.boot.ethweb3.demo;

import com.julius.spring.boot.ethweb3.Property;
import com.julius.spring.boot.ethweb3.PropertySafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.math.BigInteger;

@SuppressWarnings({"java:S112", "java:S2629"})
public class PlayAroundDemo {

	public static final Logger LOGGER = LoggerFactory.getLogger(PlayAroundDemo.class);
	public static final String ETH_SERVER_ADDRESS = "https://ethereum-holesky-rpc.publicnode.com";
	private static final String SAFE_CONTRACT_ADDRESS = "0x3be21b25ef8eca53b3de604a4a57e46569cc2e49";

	// only change these three
	public static final String EXTERNAL_PROPERTY_ID = "<PLACE_YOUR_ID_HERE>";
	private static final String PROPERTY_NAME = "<PLACE_A_NAME_HERE>";
	public static final int PROPERTY_VALUE = 100;

	/**
	 *
	 * Hi, this is the demo I mentioned in the README.
	 * You can test everything you want in the main method, the others are there for setup reasons
	 * -
	 * Everything is prepared, you can just start by choosing your PropertyName and ID (uniqueness is checked on the PropertySafe)
	 * After you chose your values, you can test the main method or extend it as needed
	 *
	 * @param args dont worry about that ;)
	 */
	public static void main(String[] args) throws Exception {

		// setup for general node query
		final Web3j web3jConnection = web3jClient();
		LOGGER.info("connected node: {}", web3jConnection.web3ClientVersion().send().getWeb3ClientVersion());

		// setup for sending transactions
		final Credentials credentials = loadCredentials();
		final TransactionManager manager = createTransactionManager(web3jConnection, credentials);
		final ContractGasProvider gasProvider = new DefaultGasProvider();

		// load the PropertySafe with the given address (pre-deployed)
		PropertySafe propertySafe = PropertySafe.load(SAFE_CONTRACT_ADDRESS, web3jConnection, manager, gasProvider);
		LOGGER.info("deployPropertyToSafe for id: {}", EXTERNAL_PROPERTY_ID);

		// deploy a new Property and add it to the PropertySafe
		Property property = Property.deploy(web3jConnection, manager, gasProvider, PROPERTY_NAME, BigInteger.valueOf(PROPERTY_VALUE)).send();
		property.setPropertyId(EXTERNAL_PROPERTY_ID).send();
		LOGGER.info("contractAddress of deployed property: {}", property.getContractAddress());
		propertySafe.addProperty(EXTERNAL_PROPERTY_ID, property.getContractAddress()).send();
		LOGGER.info("successfully added property to the PropertySafe");

		// getting the
		BigInteger value = property.value().send();
		LOGGER.info("value of deployed property: {}", value);


		//Challenge 2 from here
		property.setDescription("this is a description").send();

		LOGGER.info("new description for property wit id '{}': {}", EXTERNAL_PROPERTY_ID, property.getDescription().send());

	}

	public static Credentials loadCredentials() {
		Credentials credentials = null;
		try {
			String keyFile = "./src/main/resources/keyfile.json";
			File key = ResourceUtils.getFile(keyFile);
			LOGGER.info("keyfile path: {}", key.getPath());
			credentials = WalletUtils.loadCredentials("password", key);
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
