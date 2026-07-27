// SPDX-License-Identifier: UNLICENSED
pragma solidity 0.8.15;

interface IProperty {
    function setValue(int256 _value) external;
    function getValue() external view returns (int256);
    function setPropertyId(string memory _propertyId) external;
    function getPropertyId() external view returns (string memory);
    function setName(string memory _name) external;
    function getName() external view returns (string memory);
}

