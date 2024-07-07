package com.assignment.schedule;

import com.assignment.model.BinanceTicker;
import com.assignment.model.PriceType;
import com.assignment.service.TransactionService;
import com.assignment.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class PriceAggregationScheduler {

    private static final String BINANCE_TICKER_URL = "https://api.binance.com/api/v3/ticker/bookTicker";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TransactionService transactionService;

    @Scheduled(fixedRate = 10000) // Run every 10 seconds
    public void aggregatePrices() {
        try {
            ResponseEntity<String> binanceResponse = restTemplate.getForEntity(BINANCE_TICKER_URL, String.class);
            processBinanceResponse(binanceResponse.getBody());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processBinanceResponse(String responseBody) {
        List<BinanceTicker> tickers = JsonUtils.fromJsonToList(responseBody, BinanceTicker.class);

        for (BinanceTicker ticker : tickers) {
            if ("ETHBTC".equals(ticker.getSymbol())) {
                updateBestPrice("ETHUSDT", ticker.getBidPrice(), PriceType.BID);
            } else if ("BTCUSDT".equals(ticker.getSymbol())) {
                updateBestPrice("BTCUSDT", ticker.getAskPrice(), PriceType.ASK);
            }
        }
    }

    private void updateBestPrice(String currencyPair, String priceStr, PriceType priceType) {
        BigDecimal price = new BigDecimal(priceStr);
        transactionService.updateBestPrice(currencyPair, price, priceType);
    }
}
