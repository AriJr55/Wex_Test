package com.example.wex.vo;

import com.example.wex.entity.Transactions;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;


public class TransactionVO {

    private static final long serialVersionUID = -3760445487636086034L;

    private String description;
    private String purchaseAmount;
    private String destinyKey;
    private UUID purchaseKey;
    private LocalDateTime transactionDate;

    public TransactionVO() {
    }

    public TransactionVO(Transactions transaction) {
        this.description = transaction.getDescription();
        this.purchaseKey = transaction.getPurchaseKey();
        this.transactionDate = transaction.getTransactionDate().truncatedTo(ChronoUnit.SECONDS);
        this.purchaseAmount = (double) Math.round(transaction.getPurchaseAmount() * 100) / 100 + " U$$";
        this.destinyKey = String.valueOf(transaction.getDestinyKey());
    }

    public String getDescription() {
        return description;
    }

    public String getPurchaseAmount() {
        return purchaseAmount;
    }

    public String getDestinyKey() {
        return destinyKey;
    }

    public UUID getPurchaseKey() {
        return purchaseKey;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
}
