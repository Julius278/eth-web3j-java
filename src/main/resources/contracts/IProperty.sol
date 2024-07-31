// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

interface IProperty {
    function setValue(int _value) external;
    function getValue() external view returns (int);
    function setPropertyId(string memory _propertyId) external;
    function getPropertyId() external view returns (string memory);
    function setName(string memory _name) external;
    function getName() external view returns (string memory);
}
