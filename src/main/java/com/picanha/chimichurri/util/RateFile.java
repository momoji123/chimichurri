package com.picanha.chimichurri.util;

import java.math.BigDecimal;
import java.util.Map;

public class RateFile {
	private Map<String, Map<String, BigDecimal>> data;

	public RateFile(Map<String, Map<String, BigDecimal>> data) {
		this.data = data;
	}

	public Map<String, Map<String, BigDecimal>> getData() {
		return data;
	}

	public void setData(Map<String, Map<String, BigDecimal>> data) {
		this.data = data;
	}
}
