// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

contract Property {

    int public value;

    constructor(){
        value = 4;
    }

    function setValue(int _value) public{
        value = _value;
    }
}
