package com.picanha.chimichurri.api.pub.dto;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import com.picanha.chimichurri.entities.transaction.Transaction;

public class TransactionDTO {
	String id;
	int dbId;
	String date; // `2025-10-05T05:04:10.852+01:00`,
	TransactionType type;
	String asset;
	BigDecimal amount;
	BigDecimal price;
	BigDecimal total;
	TransactionStatus status;

	public TransactionDTO() {
	}

	public TransactionDTO(Transaction t) {
		this.id = t.getTransactionId();
		this.dbId = t.getId();
		this.date = t.getTransactionDate().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		this.type = t.getAmount().compareTo(BigDecimal.ZERO) < 0 ? TransactionType.sell : TransactionType.buy;
		this.asset = t.getAsset();
		this.amount = t.getAmount().abs();
		this.price = t.getRate();
		this.total = t.getAmount().abs().multiply(t.getRate());
		this.status = TransactionStatus.completed;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
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

}
