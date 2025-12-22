package com.picanha.chimichurri.api.pub.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.picanha.chimichurri.entities.transaction.Transaction;

public class TransactionDTO {
	private String id;
	private int dbId;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	private ZonedDateTime date; // `2025-10-05T05:04:10.852+01:00`,
	private TransactionType type;
	private String asset;
	private BigDecimal amount;
	private BigDecimal price;
	private BigDecimal total;
	private TransactionStatus status;

	public TransactionDTO() {
	}

	public TransactionDTO(Transaction t) {
		this.id = t.getTransactionId();
		this.dbId = t.getId();
		this.date = t.getTransactionDate();
		this.type = t.getAmount().compareTo(BigDecimal.ZERO) < 0 ? TransactionType.sell : TransactionType.buy;
		this.asset = t.getAsset();
		this.amount = t.getAmount().abs();
		this.price = t.getRate();
		this.total = t.getAmount().abs().multiply(t.getRate()).setScale(2, RoundingMode.HALF_UP);
		this.status = TransactionStatus.completed;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ZonedDateTime getDate() {
		return date;
	}

	public void setDate(ZonedDateTime date) {
		this.date = date;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public String getAsset() {
		return asset;
	}

	public void setAsset(String asset) {
		this.asset = asset;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public int getDbId() {
		return dbId;
	}

	public void setDbId(int dbId) {
		this.dbId = dbId;
	}

}
