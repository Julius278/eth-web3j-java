package com.julius.spring.boot.ethweb3.evm;

import com.julius.spring.boot.ethweb3.Property;
import com.julius.spring.boot.ethweb3.PropertySafe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.web3j.EVMTest;
import org.web3j.protocol.Web3j;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;

@EVMTest()
class PropertyTest {

	@Test
	void test(Web3j web3j,
					 TransactionManager transactionManager,
					 ContractGasProvider contractGasProvider) throws Exception {
		BigInteger expectedValue = BigInteger.valueOf(24);

		Property property = Property.deploy(web3j, transactionManager, contractGasProvider, "HansDieter", expectedValue).send();

		BigInteger value = property.getValue().send();

		Assertions.assertEquals(expectedValue, value);
	}

	@Test
	void testRegistry(Web3j web3j,
					 TransactionManager transactionManager,
					 ContractGasProvider contractGasProvider) throws Exception {
		PropertySafe deployedPropertySafe = PropertySafe.deploy(web3j, transactionManager, contractGasProvider).send();
	}
}
