// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

interface IProperty {
    function setValue(int _value) external;
    function getValue() external returns (int);
    function setPropertyId(string memory _propertyId) external;
    function getPropertyId() external returns (string memory);
}
