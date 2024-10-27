package com.example.wex.vo;

import com.example.wex.entity.Transactions;
import com.example.wex.utils.JSONUtils;
import org.springframework.http.ResponseEntity;

import java.util.Map;


public class TransactionConvertedVO extends TransactionVO {

    private String exchangeRate;
    private String convertedAmount;
    private String countryCurrencyDesc;


    public TransactionConvertedVO() {
    }

    public TransactionConvertedVO(Transactions transaction, Map<String,Object> currencyData) {
        super(transaction);
        this.exchangeRate = (String) currencyData.get("exchange_rate");
        Double value = (double) Math.round(Double.parseDouble((String) currencyData.get("exchange_rate")) * 100) / 100;
        this.convertedAmount = String.valueOf(value);
        this.countryCurrencyDesc = (String) currencyData.get("country_currency_desc");
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public String getConvertedAmount() {
        return convertedAmount;
    }

    public String getCountryCurrencyDesc() {
        return countryCurrencyDesc;
    }
}
