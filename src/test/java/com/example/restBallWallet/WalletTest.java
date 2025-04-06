package com.example.restBallWallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.restBallWallet.model.Wallet;
import com.example.restBallWallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class WalletTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WalletRepository walletRepository;

    private final UUID walletId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @BeforeEach
    public void setUp() {
        walletRepository.deleteAll();
        walletRepository.save(new Wallet(walletId, BigDecimal.valueOf(1000)));
    }

    @Test
    public void testDeposit() throws Exception {
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"walletId\":\"123e4567-e89b-12d3-a456-426614174000\",\"typeOfOperation\":\"DEPOSIT\",\"amount\":500}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1500));
    }

    @Test
    public void testWithdrawSuccess() throws Exception {
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"walletId\":\"123e4567-e89b-12d3-a456-426614174000\",\"typeOfOperation\":\"WITHDRAW\",\"amount\":300}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(700));
    }

    @Test
    public void testWithdrawInsufficientFunds() throws Exception {
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"walletId\":\"123e4567-e89b-12d3-a456-426614174000\",\"typeOfOperation\":\"WITHDRAW\",\"amount\":5000}"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testInvalidWallet() throws Exception {
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"walletId\":\"00000000-0000-0000-0000-000000000000\",\"typeOfOperation\":\"DEPOSIT\",\"amount\":100}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testInvalidJson() throws Exception {
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid-json"))
                .andExpect(status().isBadRequest());
    }
    @Autowired
    private Environment environment;

    @Test
    void checkActiveProfile() {
        String[] profiles = environment.getActiveProfiles();
        System.out.println("ACTIVE PROFILES: " + Arrays.toString(profiles));
        assertTrue(Arrays.asList(profiles).contains("test"));
    }
    @Test
    void printDatasourceUrl() {
        System.out.println("spring.datasource.url = " + environment.getProperty("spring.datasource.url"));
        System.out.println("spring.datasource.username = " + environment.getProperty("spring.datasource.username"));
        System.out.println("spring.datasource.password = " + environment.getProperty("spring.datasource.password"));
    }

}

