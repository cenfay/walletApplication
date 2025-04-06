package com.example.restBallWallet.controller;

import com.example.restBallWallet.model.enums.TypeOfOperation;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class WalletRequest {

    @NotNull(message = "Wallet ID cannot be null")
    private UUID walletId;

    @NotNull(message = "Operation type cannot be null")
    private TypeOfOperation typeOfOperation;

    @NotNull(message = "Amount cannot be null")
    private BigDecimal amount;
}