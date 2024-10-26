package com.example.wex.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="transactions")
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "purchase_amount", nullable = false)
    private Double purchaseAmount;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

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

    @PrePersist
    void prePersist() {
        if (this.transactionDate == null) {
            this.transactionDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    void preUpdate() {
        this.transactionDate = LocalDateTime.now();;
    }
}
