package com.assignment.service;

import com.assignment.model.Wallet;
import com.assignment.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    public Map<String, BigDecimal> getWalletBalances(Long userId) {
        List<Wallet> wallets = walletRepository.findByUserId(userId);
        Map<String, BigDecimal> balances = new HashMap<>();
        for (Wallet wallet : wallets) {
            balances.put(wallet.getCurrency(), wallet.getBalance());
        }
        return balances;
    }
}

