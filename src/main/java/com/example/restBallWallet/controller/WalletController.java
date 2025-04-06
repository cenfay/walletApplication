package com.example.restBallWallet.controller;

import com.example.restBallWallet.exception.InsufficientFundsException;
import com.example.restBallWallet.exception.InvalidOperationException;
import com.example.restBallWallet.exception.WalletNotFoundException;
import com.example.restBallWallet.model.Wallet;
import com.example.restBallWallet.repository.WalletRepository;
import com.example.restBallWallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1")
public class WalletController {

    private final WalletRepository walletRepository;
    private final WalletService walletService;


    @PostMapping("/wallet")
    public ResponseEntity<Wallet> performTheOperation(@RequestBody @Valid WalletRequest request) {
        try {
            Wallet wallet = walletService.processTransaction(request.getWalletId(), request.getTypeOfOperation(), request.getAmount());
            return ResponseEntity.ok(wallet);
        } catch (WalletNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).header("ERROR", "WalletNotFoundException").body(null);
        } catch (InsufficientFundsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).header("ERROR", "InsufficientFundsException").body(null);
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("ERROR", "InvalidOperationException").body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }


    @GetMapping("/wallets/{walletId}")
    public ResponseEntity<Wallet> getBalance(@PathVariable UUID walletId) {
        try {
            Wallet wallet = walletService.getWalletBalance(walletId);
            return ResponseEntity.ok(wallet);
        } catch (WalletNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Не найден кошелёк с id = " + walletId);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
