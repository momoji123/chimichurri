package com.picanha.chimichurri.api.pub.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.picanha.chimichurri.util.KursGenerator;

public class TransactionWithSummaryDTO {

	private List<TransactionDTO> transactions = new ArrayList<>();
	private Map<String, TransactionSummary> summary = new HashMap<>();

	public TransactionWithSummaryDTO() {

	}

	public TransactionWithSummaryDTO(List<TransactionDTO> transactions, boolean withSummary) {
		this.transactions = transactions;
		if (withSummary) {
			generateSummaryMap();
		}
	}

	public void generateSummaryMap() {
		Map<String, TransactionSummary> result = new HashMap<>();
		for (TransactionDTO t : transactions) {
			TransactionSummary summary = result.get(t.getAsset());
			if (summary == null) {
				summary = new TransactionSummary();
				result.put(t.getAsset(), summary);
			}

			BigDecimal purchasePrice = t.getAmount().multiply(t.getPrice()).setScale(2, RoundingMode.HALF_UP);

			if (t.getType().equals(TransactionType.sell)) {
				summary.setTotalAmount(summary.getTotalAmount().subtract(t.getAmount()));
				summary.setTotalPurchase(summary.getTotalPurchase().subtract(purchasePrice));
			} else {
				summary.setTotalAmount(summary.getTotalAmount().add(t.getAmount()));
				summary.setTotalPurchase(summary.getTotalPurchase().add(purchasePrice));
			}
		}

		for (String asset : result.keySet()) {
			TransactionSummary summary = result.get(asset);
			summary.setAvgSharePrice(
					summary.getTotalAmount().divide(summary.getTotalAmount(), RoundingMode.HALF_UP).setScale(2));
			summary.setCurrentRate(KursGenerator.getKurs(asset, LocalDate.now()).setScale(2, RoundingMode.HALF_UP));
			summary.setCurrentValue(
					summary.getTotalAmount().multiply(summary.getCurrentRate()).setScale(2, RoundingMode.HALF_UP));
		}

		this.summary = result;
	}

	public List<TransactionDTO> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<TransactionDTO> transactions) {
		this.transactions = transactions;
	}

	public Map<String, TransactionSummary> getSummary() {
		return summary;
	}

	public void setSummary(Map<String, TransactionSummary> summary) {
		this.summary = summary;
	}

}
