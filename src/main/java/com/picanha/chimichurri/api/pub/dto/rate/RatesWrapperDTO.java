package com.picanha.chimichurri.api.pub.dto.rate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class RatesWrapperDTO {
	private String currency;
	private Map<String, LinkedHashMap<LocalDate, BigDecimal>> rates = new HashMap<>();

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Map<String, LinkedHashMap<LocalDate, BigDecimal>> getRates() {
		return rates;
	}

	public void setRates(Map<String, LinkedHashMap<LocalDate, BigDecimal>> rates) {
		this.rates = rates;
	}

	public void addRate(String asset, LocalDate date, BigDecimal rate) {
		if (rates.get(asset) == null) {
			rates.put(asset, new LinkedHashMap<>());
		}

		rates.get(asset).put(date, rate);
	}

}
