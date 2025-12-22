package com.picanha.chimichurri.api.pub.dto;

import java.math.BigDecimal;

public class TransactionSummary {
	private BigDecimal totalAmount = BigDecimal.ZERO;
	private BigDecimal totalPurchase = BigDecimal.ZERO;
	private BigDecimal avgSharePrice = BigDecimal.ZERO;
	private BigDecimal currentRate = BigDecimal.ZERO;
	private BigDecimal currentValue = BigDecimal.ZERO;

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getTotalPurchase() {
		return totalPurchase;
	}

	public void setTotalPurchase(BigDecimal totalPurchase) {
		this.totalPurchase = totalPurchase;
	}

	public BigDecimal getAvgSharePrice() {
		return avgSharePrice;
	}

	public void setAvgSharePrice(BigDecimal avgSharePrice) {
		this.avgSharePrice = avgSharePrice;
	}

	public BigDecimal getCurrentRate() {
		return currentRate;
	}

	public void setCurrentRate(BigDecimal currentRate) {
		this.currentRate = currentRate;
	}

	public BigDecimal getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(BigDecimal currentValue) {
		this.currentValue = currentValue;
	}

}
