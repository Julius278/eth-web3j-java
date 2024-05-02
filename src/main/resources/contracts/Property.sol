// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

contract Property {

    string internal id;
    int public value;

    constructor(int _value){
        value = _value;
    }

    function setValue(int _value) public{
        value = _value;
    }

    function setPropertyId(string memory _propertyId) public{
        id = _propertyId;
    }

}
