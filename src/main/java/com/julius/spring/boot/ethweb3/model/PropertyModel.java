package com.julius.spring.boot.ethweb3.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

@Getter
@Setter
@ToString
public class PropertyModel {
    private String id;
    private String name;
    private BigInteger value;
}
