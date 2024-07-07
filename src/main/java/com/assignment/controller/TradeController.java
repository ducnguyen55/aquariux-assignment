package com.assignment.controller;

import com.assignment.model.TradeRequest;
import com.assignment.model.Transaction;
import com.assignment.model.TransactionType;
import com.assignment.model.User;
import com.assignment.service.TransactionService;
import com.assignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/trade")
public class TradeController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @PostMapping("/trade")
    public ResponseEntity<String> executeTrade(@RequestBody TradeRequest tradeRequest) {
        Long userId = tradeRequest.getUserId();
        String cryptoPair = tradeRequest.getCryptoPair();
        TransactionType transactionType = tradeRequest.getTransactionType();
        BigDecimal quantity = tradeRequest.getQuantity();

        if (userId == null || cryptoPair == null || transactionType == null || quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body("Invalid trade request");
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if (transactionType == TransactionType.SELL) {
            BigDecimal bidPrice = fetchBidPriceFromDatabase(cryptoPair);
            BigDecimal totalTransactionAmount = bidPrice.multiply(quantity);
            if (user.getWalletBalance().compareTo(totalTransactionAmount) < 0) {
                return ResponseEntity.badRequest().body("Insufficient balance for transaction");
            }
        }

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setCurrencyPair(cryptoPair);
        transaction.setTransactionType(transactionType);
        transaction.setQuantity(quantity);

        transaction.setPrice(BigDecimal.ZERO);

        transactionService.saveTransaction(transaction);

        if (transactionType == TransactionType.SELL) {
            BigDecimal transactionAmount = transaction.getPrice().multiply(quantity);
            BigDecimal newBalance = user.getWalletBalance().subtract(transactionAmount);
            user.setWalletBalance(newBalance);
            userService.updateUser(user);
        }

        return ResponseEntity.ok("Trade executed successfully");
    }

    private BigDecimal fetchBidPriceFromDatabase(String cryptoPair) {
        return BigDecimal.valueOf(10000.00);
    }
}

