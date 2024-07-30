// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

import {IProperty} from "./IProperty.sol";

contract Property is IProperty {

    string internal id;
    int public value;

    constructor(int _value){
        value = _value;
    }

    function setValue(int _value) public{
        value = _value;
    }

    function getValue() public view returns (int) {
        return value;
    }

    function setPropertyId(string memory _propertyId) public{
        id = _propertyId;
    }

    function getPropertyId() public view returns (string memory){
        return id;
    }

}
