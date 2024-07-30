package com.julius.spring.boot.ethweb3.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class TransferRequest {
    private String recipient;
    private BigDecimal amountInEth;
}
