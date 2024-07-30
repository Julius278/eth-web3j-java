package com.julius.spring.boot.ethweb3.service;

import com.julius.spring.boot.ethweb3.Property;
import com.julius.spring.boot.ethweb3.PropertySafe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertySafeServiceTest {

	@InjectMocks
	private PropertySafeService propertySafeService;

	@Mock
	private Web3j web3j;

	@Mock
	private ContractGasProvider contractGasProvider;

	@Mock
	private TransactionManager transactionManager;

	@Mock
	private PropertySafe propertySafe;

	@Test
	void deployPropertyToSafe() throws Exception {
		//given
		String contractAddress = "0x12345";
		String externalPropertyId = "test123";
		int propertyValue = 300;

		//when
		try (MockedStatic<Property> property = Mockito.mockStatic(Property.class)) {
			Property propertyMock = Mockito.mock(Property.class);
			var deployPropertyCall = mock(RemoteFunctionCall.class);
			property.when(
					() -> Property.deploy(web3j, transactionManager, contractGasProvider, BigInteger.valueOf(propertyValue)))
				.thenReturn(deployPropertyCall);
			when(deployPropertyCall.send()).thenReturn(propertyMock);

			var setPropertyIdCall = mock(RemoteFunctionCall.class);
			when(propertyMock.setPropertyId(externalPropertyId)).thenReturn(setPropertyIdCall);
			when(setPropertyIdCall.send()).thenReturn("");
			when(propertyMock.getContractAddress()).thenReturn(contractAddress);

			var addPropertyCall = mock(RemoteFunctionCall.class);
			when(propertySafe.addProperty(externalPropertyId, contractAddress)).thenReturn(addPropertyCall);
			when(addPropertyCall.send()).thenReturn("");

			var getValueCall = mock(RemoteFunctionCall.class);
			when(propertyMock.value()).thenReturn(getValueCall);
			when(getValueCall.send()).thenReturn(BigInteger.valueOf(propertyValue));

			//then
			assertEquals(BigInteger.valueOf(propertyValue), propertySafeService.deployPropertyToSafe(externalPropertyId, propertyValue));
		}

	}
}