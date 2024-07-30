// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

import {Property} from "./Property.sol";

contract PropertySafe {

    modifier propertyAddressesEmpty() {
        require(propertyAddresses.length == 0, "There are already addresses saved");
        _;
    }

    int public value;
    mapping(string => address) public propertyMap;
    address[] private propertyAddresses;


    function createProperty(string memory _externalPropertyId, int _value) external {
        require(propertyMap[_externalPropertyId] == address(0));

        Property property = new Property(_value);
        property.setPropertyId(_externalPropertyId);
        address propertyAddress = address(property);
        propertyAddresses.push(propertyAddress);
        propertyMap[_externalPropertyId] = propertyAddress;
    }

    function addProperty(string memory _externalPropertyId, address _propertyAddress) external {
        require(propertyMap[_externalPropertyId] == address(0), "externalPropertyId already in use");

        propertyAddresses.push(_propertyAddress);
        propertyMap[_externalPropertyId] = _propertyAddress;
    }

    function getPropertyById(string memory _externalPropertyId) external view returns(address){
        return propertyMap[_externalPropertyId];
    }

    function getProperties() external view returns(address[] memory) {
        return propertyAddresses;
    }

    function getPropertyAmount() external view returns(uint256) {
        return propertyAddresses.length;
    }

}
