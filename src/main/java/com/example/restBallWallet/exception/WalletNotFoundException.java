package com.example.restBallWallet.exception;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException(String message) {

        super(message);
    }
}
