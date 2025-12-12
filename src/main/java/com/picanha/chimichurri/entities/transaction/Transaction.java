package com.picanha.chimichurri.entities.transaction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.picanha.chimichurri.entities.account.Account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "TRANSACTION", schema = "chimichurri")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRANSACTION_SEQ")
    @SequenceGenerator(schema="chimichurri", name="TRANSACTION_SEQ", sequenceName="TRANSACTION_SEQ", initialValue=1, allocationSize=1)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "ACCOUNTID",
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_TRANSACTION_ACCOUNT")
    )
    private Account account;

    @Column(name = "TRANSACTION_ID", length = 50)
    private String transactionId;

    @Column(name = "TRANSACTION_DATE", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm z")
    private ZonedDateTime transactionDate;

    @Column(name = "BASE_ASSET", length = 50)
    private String baseAsset;

    @Column(name = "ASSET", length = 50)
    private String asset;

    @Column(name = "RATE", precision = 19, scale = 2)
    private BigDecimal rate;

    @Column(name = "AMOUNT", precision = 19, scale = 2)
    private BigDecimal amount;

    // Getters and setters (or use Lombok)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public ZonedDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(ZonedDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getBaseAsset() {
        return baseAsset;
    }

    public void setBaseAsset(String baseAsset) {
        this.baseAsset = baseAsset;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
