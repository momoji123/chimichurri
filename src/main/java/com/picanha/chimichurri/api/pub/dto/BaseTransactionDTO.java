package com.picanha.chimichurri.api.pub.dto;

import java.math.BigDecimal;

public class BaseTransactionDTO {
	protected String asset;
	protected TransactionType type;
	protected BigDecimal amount;

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
}
