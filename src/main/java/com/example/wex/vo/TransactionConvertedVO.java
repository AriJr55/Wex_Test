package com.example.wex.vo;

import com.example.wex.entity.Transactions;

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
        Double value = Double.parseDouble((String) currencyData.get("exchange_rate")) * transaction.getPurchaseAmount();
        this.convertedAmount = String.valueOf((double) Math.round(value * 100 / 100 ));
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
