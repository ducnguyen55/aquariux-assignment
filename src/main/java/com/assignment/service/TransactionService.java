package com.assignment.service;

import com.assignment.model.PriceType;
import com.assignment.model.Transaction;
import com.assignment.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public void updateBestPrice(String currencyPair, BigDecimal price, PriceType priceType) {
        Transaction latestTransaction = transactionRepository.findFirstByCurrencyPairOrderByTimestampDesc(currencyPair);

        if (latestTransaction == null) {
            latestTransaction = new Transaction();
            latestTransaction.setCurrencyPair(currencyPair);
            latestTransaction.setTimestamp(LocalDateTime.now());
            latestTransaction.setUser(null);
        }

        if (priceType == PriceType.BID) {
            latestTransaction.setBidPrice(price);
        } else if (priceType == PriceType.ASK) {
            latestTransaction.setAskPrice(price);
        }

        transactionRepository.save(latestTransaction);
    }

    public List<Transaction> getTradingHistory(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getUserTransactions(Long userId) {
        return transactionRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    @Transactional
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
