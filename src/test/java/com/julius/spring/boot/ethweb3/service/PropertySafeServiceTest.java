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
import org.web3j.tx.exceptions.ContractCallException;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.List;

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
		String propertyName = "test123Name";
		int propertyValue = 300;

		//when
		try (MockedStatic<Property> property = Mockito.mockStatic(Property.class)) {
			Property propertyMock = Mockito.mock(Property.class);
			var deployPropertyCall = mock(RemoteFunctionCall.class);
			property.when(
					() -> Property.deploy(web3j, transactionManager, contractGasProvider, propertyName, BigInteger.valueOf(propertyValue)))
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
			assertEquals(BigInteger.valueOf(propertyValue), propertySafeService.deployPropertyToSafe(externalPropertyId, propertyName, propertyValue));
		}
	}

	@Test
	void deployPropertyToSafe_ContractCallException() throws Exception {
		//given
		String externalPropertyId = "test123";
		String propertyName = "test123Name";
		int propertyValue = 300;

		//when
		try (MockedStatic<Property> property = Mockito.mockStatic(Property.class)) {
			var deployPropertyCall = mock(RemoteFunctionCall.class);
			property.when(
					() -> Property.deploy(web3j, transactionManager, contractGasProvider, propertyName, BigInteger.valueOf(propertyValue)))
				.thenReturn(deployPropertyCall);
			when(deployPropertyCall.send()).thenThrow(new Exception());

			//then
			assertThrows(ContractCallException.class, () -> propertySafeService.deployPropertyToSafe(externalPropertyId, propertyName, propertyValue));
		}
	}

	@Test
	void deployPropertySafe() throws Exception {
		//given
		String contractAddress = "0x12345";

		//when
		try (MockedStatic<PropertySafe> safe = Mockito.mockStatic(PropertySafe.class)) {
			PropertySafe propertySafeMock = Mockito.mock(PropertySafe.class);
			var deployPropertySafeCall = mock(RemoteFunctionCall.class);
			safe.when(
					() -> PropertySafe.deploy(web3j, transactionManager, contractGasProvider))
				.thenReturn(deployPropertySafeCall);
			when(deployPropertySafeCall.send()).thenReturn(propertySafeMock);

			when(propertySafeMock.getContractAddress()).thenReturn(contractAddress);

			//then
			assertEquals(contractAddress, propertySafeService.deployPropertySafe());
		}
	}

	@Test
	void deployPropertySafe_ContractCallException() throws Exception {
		//when
		try (MockedStatic<PropertySafe> safe = Mockito.mockStatic(PropertySafe.class)) {
			var deployPropertySafeCall = mock(RemoteFunctionCall.class);
			safe.when(
					() -> PropertySafe.deploy(web3j, transactionManager, contractGasProvider))
				.thenReturn(deployPropertySafeCall);
			when(deployPropertySafeCall.send()).thenThrow(new Exception());

			//then
			assertThrows(ContractCallException.class, () -> propertySafeService.deployPropertySafe());
		}
	}

	@Test
	void listProperties() throws Exception {
		//given
		String propertyId = "testId";
		String propertyName = "testName";
		BigInteger propertyValue = BigInteger.valueOf(1);

		var getPropertiesCall = mock(RemoteFunctionCall.class);
		when(propertySafe.getProperties()).thenReturn(getPropertiesCall);
		when(getPropertiesCall.send()).thenReturn(getMockedPropertiesList());

		try (MockedStatic<Property> property = Mockito.mockStatic(Property.class)) {
			Property propertyMock = Mockito.mock(Property.class);
			property.when(
					() -> Property.load("0x01", web3j, transactionManager, contractGasProvider))
				.thenReturn(propertyMock);
			property.when(
					() -> Property.load("0x02", web3j, transactionManager, contractGasProvider))
				.thenReturn(propertyMock);
			property.when(
					() -> Property.load("0x03", web3j, transactionManager, contractGasProvider))
				.thenReturn(propertyMock);

			var getPropertyIdCall = mock(RemoteFunctionCall.class);
			when(propertyMock.getPropertyId()).thenReturn(getPropertyIdCall);
			when(getPropertyIdCall.send()).thenReturn(propertyId);

			var getPropertyNameCall = mock(RemoteFunctionCall.class);
			when(propertyMock.getName()).thenReturn(getPropertyNameCall);
			when(getPropertyNameCall.send()).thenReturn(propertyName);

			var getPropertyValueCall = mock(RemoteFunctionCall.class);
			when(propertyMock.getValue()).thenReturn(getPropertyValueCall);
			when(getPropertyValueCall.send()).thenReturn(propertyValue);


			//then
			List response = propertySafeService.listPropertySafe();
			assertEquals(getMockedPropertiesList().size(), response.size());
		}
	}

	@Test
	void listProperties_ContractCallException() throws Exception {
		var getPropertiesCall = mock(RemoteFunctionCall.class);
		when(propertySafe.getProperties()).thenReturn(getPropertiesCall);
		when(getPropertiesCall.send()).thenThrow(new Exception());

		//then
		assertThrows(ContractCallException.class, () -> propertySafeService.listPropertySafe());
	}

	private List<String> getMockedPropertiesList() {
		return List.of("0x01", "0x02", "0x03");
	}
}