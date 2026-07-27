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
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Properties;

@SuppressWarnings("java:S112")
public class PipelineLocalTestPrepDemo {

	public static final Logger LOGGER = LoggerFactory.getLogger(PipelineLocalTestPrepDemo.class);
	public static final String KEY_FILE_PATH = "./src/main/resources/keyfile.json";
	public static final String ETH_SERVER_ADDRESS = "http://localhost:8545";

	public static final String EXTERNAL_PROPERTY_ID = "dummy-external-property-id";
	private static final String PROPERTY_NAME = "dummy-property-name";
	public static final int PROPERTY_VALUE = 100;
	// used for GitHub Pipeline environment export
	public static final String DEPLOYED_CONTRACTS_FILE = "deployed-contracts.env";

	// Gas parameters optimized for Besu dev mode
	private static final BigInteger GAS_PRICE = BigInteger.valueOf(1_000_000_000L); // 1 Gwei
	private static final BigInteger GAS_LIMIT = BigInteger.valueOf(3_000_000L); // 3 Million gas


	public static void main(String[] args) throws Exception {

		// setup for general node query
		final Web3j web3jConnection = web3jClient();
		LOGGER.info("connected node: {}", web3jConnection.web3ClientVersion().send().getWeb3ClientVersion());
		LOGGER.info("current block number: {}", web3jConnection.ethBlockNumber().send().getBlockNumber());

		// setup for sending transactions
		final Credentials credentials = loadCredentials(KEY_FILE_PATH);
		final ContractGasProvider gasProvider = new StaticGasProvider(GAS_PRICE, GAS_LIMIT);
		
		LOGGER.info("Using StaticGasProvider with gasPrice: {} wei, gasLimit: {}", GAS_PRICE, GAS_LIMIT);

		// load the PropertySafe with the given address (pre-deployed)
		LOGGER.info("Deploying PropertySafe...");
		PropertySafe propertySafe = PropertySafe.deploy( web3jConnection, credentials, gasProvider).send();
		LOGGER.info("property deployed on address: {}", propertySafe.getContractAddress());
		LOGGER.info("deployPropertyToSafe for id: {} on propertySafe: {}", EXTERNAL_PROPERTY_ID, propertySafe.getContractAddress());

		// deploy a new Property and add it to the PropertySafe
		LOGGER.info("Deploying Property...");
		Property property = Property.deploy(web3jConnection, credentials, gasProvider, PROPERTY_NAME, BigInteger.valueOf(PROPERTY_VALUE)).send();
		LOGGER.info("contractAddress of deployed property: {}", property.getContractAddress());
		
		LOGGER.info("Setting property ID...");
		property.setPropertyId(EXTERNAL_PROPERTY_ID).send();
		
		LOGGER.info("Adding property to PropertySafe...");
		propertySafe.addProperty(EXTERNAL_PROPERTY_ID, property.getContractAddress()).send();
		LOGGER.info("successfully added property to the PropertySafe");

		// Vertragsadressen in Datei schreiben, damit Folge-Steps in der Pipeline sie nutzen können
		saveDeployedContracts(propertySafe.getContractAddress(), property.getContractAddress());
	}


	/**
	 * Persists the deployed contract addresses to a properties file ({@value #DEPLOYED_CONTRACTS_FILE}).
	 * <p>
	 * Intended for use in CI/CD pipelines: after this method completes, a subsequent pipeline step
	 * can read the file and export the addresses as environment variables (e.g. via {@code $GITHUB_ENV}),
	 * making them available to all following steps for further verification or integration tests.
	 *
	 * @param propertySafeAddress the on-chain address of the deployed {@code PropertySafe} contract
	 * @param propertyAddress     the on-chain address of the deployed {@code Property} contract
	 * @throws IOException if the file cannot be written
	 */
	public static void saveDeployedContracts(String propertySafeAddress, String propertyAddress) throws IOException {
		Properties props = new Properties();
		props.setProperty("PROPERTY_SAFE_ADDRESS", propertySafeAddress);
		props.setProperty("PROPERTY_ADDRESS", propertyAddress);

		try (FileWriter writer = new FileWriter(DEPLOYED_CONTRACTS_FILE)) {
			props.store(writer, "Deployed contract addresses");
		}

		LOGGER.info("Contract addresses saved to {}", DEPLOYED_CONTRACTS_FILE);
		LOGGER.info("  PROPERTY_SAFE_ADDRESS={}", propertySafeAddress);
		LOGGER.info("  PROPERTY_ADDRESS={}", propertyAddress);
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
}
