package com.julius.spring.boot.ethweb3.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthBlockNumber;

import java.io.IOException;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Web3ServiceTest {

	@InjectMocks
	private Web3Service web3Service;

	@Mock
	private Web3j web3c;

	@Test
	void getEthBlockNumber() throws IOException {
		//given
		BigInteger blockNumber = BigInteger.valueOf(12345);
		EthBlockNumber ethBlockNumber = new EthBlockNumber();
		ethBlockNumber.setResult(blockNumber.toString());

		//when
		var blockNumberCall = mock(Request.class);
		when(web3c.ethBlockNumber()).thenReturn(blockNumberCall);
		when(blockNumberCall.send()).thenReturn(ethBlockNumber);

		//then
		var ethBlock = web3Service.getEthBlockNumber();
		assertEquals(blockNumber, ethBlock.getBlockNumber());
	}
}