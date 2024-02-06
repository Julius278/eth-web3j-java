package com.julius.spring.boot.ethweb3.service;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferRequest {
    private String privateKey;
    private String recipient;
    private BigDecimal amountInEth;
}
