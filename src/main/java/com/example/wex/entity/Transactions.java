package com.example.wex.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="transactions")
public class Transactions {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "purchase_key", nullable = false)
    private UUID purchaseKey;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "purchase_amount", nullable = false)
    private Double purchaseAmount;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "destiny_key", nullable = false)
    private Long destinyKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(Double purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Long getDestinyKey() {
        return destinyKey;
    }

    public void setDestinyKey(Long destinyKey) {
        this.destinyKey = destinyKey;
    }

    public UUID getPurchaseKey() {
        return purchaseKey;
    }

    public void setPurchaseKey(UUID purchaseKey) {
        this.purchaseKey = purchaseKey;
    }

    @PrePersist
    void prePersist() {
        if (this.transactionDate == null) {
            this.transactionDate = LocalDateTime.now();
        }

        if (this.purchaseKey == null) {
            this.purchaseKey = UUID.randomUUID();
        }
    }

    @PreUpdate
    void preUpdate() {
        this.transactionDate = LocalDateTime.now();;
    }
}
