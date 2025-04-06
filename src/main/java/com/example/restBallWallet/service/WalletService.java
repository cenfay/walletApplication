package com.example.restBallWallet.service;

import com.example.restBallWallet.exception.InsufficientFundsException;
import com.example.restBallWallet.exception.WalletNotFoundException;
import com.example.restBallWallet.model.enums.TypeOfOperation;
import jakarta.transaction.Transactional;
import com.example.restBallWallet.model.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.restBallWallet.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.UUID;


@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Transactional
    public Wallet processTransaction(UUID walletId, TypeOfOperation typeOfOperation, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Wallet wallet = walletRepository.findByIdForUpdate(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));

        switch (typeOfOperation) {
            case DEPOSIT:
                wallet.setBalance(wallet.getBalance().add(amount));
                break;
            case WITHDRAW:
                if (wallet.getBalance().compareTo(amount) < 0) {
                    throw new InsufficientFundsException("Insufficient funds");
                }
                wallet.setBalance(wallet.getBalance().subtract(amount));
                break;
            default:
                throw new IllegalArgumentException("Invalid operation type");
        }

        return walletRepository.save(wallet);
    }


    public Wallet getWalletBalance(UUID walletId) {
        return walletRepository.findById(walletId).orElseThrow(() ->
                new WalletNotFoundException("Wallet not found"));
    }

}

