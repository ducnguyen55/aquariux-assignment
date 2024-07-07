package com.assignment.controller;

import com.assignment.model.Transaction;
import com.assignment.service.TransactionService;
import com.assignment.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/wallet/balance")
    public ResponseEntity<Map<String, BigDecimal>> getWalletBalance(@RequestParam Long userId) {
        Map<String, BigDecimal> balances = walletService.getWalletBalances(userId);
        return ResponseEntity.ok(balances);
    }

    @GetMapping("/trading/history")
    public ResponseEntity<List<Transaction>> getTradingHistory(@RequestParam Long userId) {
        List<Transaction> history = transactionService.getTradingHistory(userId);
        return ResponseEntity.ok(history);
    }
}
