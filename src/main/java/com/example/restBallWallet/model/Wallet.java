package com.example.restBallWallet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "wallet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    @Id
    @Column(name = "wallet_id")
    private UUID walletId;

    @Column(nullable = false)
    private BigDecimal balance;


    public Wallet(UUID walletId) {
        this.walletId = walletId;
        this.balance = BigDecimal.ZERO;
    }
}

