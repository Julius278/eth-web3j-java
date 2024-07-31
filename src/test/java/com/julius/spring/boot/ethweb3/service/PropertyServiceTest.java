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
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.exceptions.ContractCallException;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PropertyServiceTest {

	@InjectMocks
	private PropertyService propertyService;

	@Mock
	private Web3j web3j;

	@Mock
	private ContractGasProvider contractGasProvider;

	@Mock
	private Credentials credentials;

	@Mock
	private PropertySafe propertySafe;

	@Mock
	private Property property;


	@Test
	void getPropertyName() throws Exception {
		//given
		String propertyId = "testId";
		String propertyName = "testName";
		String propertyContractAddress = "0x01";

		var retrievePropertyCall = mock(RemoteFunctionCall.class);
		when(propertySafe.getPropertyById(propertyId)).thenReturn(retrievePropertyCall);
		when(retrievePropertyCall.send()).thenReturn(propertyContractAddress);

		try (MockedStatic<Property> propertyMockedStatic = Mockito.mockStatic(Property.class)) {
			propertyMockedStatic.when(
					() -> Property.load(propertyContractAddress, web3j, credentials, contractGasProvider))
				.thenReturn(property);
			var getNameCall = mock(RemoteFunctionCall.class);
			when(property.getName()).thenReturn(getNameCall);
			when(getNameCall.send()).thenReturn(propertyName);

			//then
			assertEquals(propertyName, propertyService.getPropertyName(propertyId));
		}
	}

	@Test
	void getPropertyNameOrValue_Exception() throws Exception {
		//given
		String propertyId = "testId";

		//when
		var retrievePropertyCall = mock(RemoteFunctionCall.class);
		when(propertySafe.getPropertyById(propertyId)).thenReturn(retrievePropertyCall);
		when(retrievePropertyCall.send()).thenThrow(new Exception());

		//then
		assertThrows(RuntimeException.class, () -> propertyService.getPropertyName(propertyId));
		assertThrows(RuntimeException.class, () -> propertyService.getPropertyValue(propertyId));
	}

	@Test
	void getPropertyValue() throws Exception {
		//given
		String propertyId = "testId";
		BigInteger propertyValue = BigInteger.valueOf(28);
		String propertyContractAddress = "0x01";

		var retrievePropertyCall = mock(RemoteFunctionCall.class);
		when(propertySafe.getPropertyById(propertyId)).thenReturn(retrievePropertyCall);
		when(retrievePropertyCall.send()).thenReturn(propertyContractAddress);

		try (MockedStatic<Property> propertyMockedStatic = Mockito.mockStatic(Property.class)) {
			propertyMockedStatic.when(
					() -> Property.load(propertyContractAddress, web3j, credentials, contractGasProvider))
				.thenReturn(property);
			var getValueCall = mock(RemoteFunctionCall.class);
			when(property.getValue()).thenReturn(getValueCall);
			when(getValueCall.send()).thenReturn(propertyValue);

			//then
			assertEquals(propertyValue, propertyService.getPropertyValue(propertyId));
		}
	}
}