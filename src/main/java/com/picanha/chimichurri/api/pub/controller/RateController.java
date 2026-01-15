package com.picanha.chimichurri.api.pub.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.picanha.chimichurri.CryptoConfigService;
import com.picanha.chimichurri.api.pub.controller.configs.CustomRequestException;
import com.picanha.chimichurri.api.pub.dto.rate.RatesWrapperDTO;
import com.picanha.chimichurri.util.KursGenerator;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/rate")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE }, allowedHeaders = { "*" })
public class RateController {

	@Autowired
	private CryptoConfigService cryptoConfig;

	@GetMapping(path = "/available")
	private String[] getAllAvailableAssets() {
		return cryptoConfig.getAllAcceptedCrypto();
	}

	@Operation(summary = "Historic daily rate of a currency", description = "Rates are randomly created as a dummy (has no corellation with real life currency rate). Date must be in ISO.DATE Format (Ex: '2007-12-15')")
	@GetMapping()
	private RatesWrapperDTO getDailyRate(
			@RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate, // 2007-12-15
			@RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate, // 2007-12-15
			@RequestParam(required = true) List<String> assets, HttpServletRequest request) {

		for (String asset : assets) {
			if (!cryptoConfig.isCryptoAssetAcceptable(asset)) {
				throw new CustomRequestException(String.format("Crypto %s asset is currently not available", asset));
			}
		}

		RatesWrapperDTO result = new RatesWrapperDTO();
		result.setCurrency("EUR");
		try {
			for (String asset : assets) {
				Map<String, BigDecimal> kurs = KursGenerator.getKurs(asset, fromDate, toDate);
				List<String> sortedKey = new ArrayList<String>(kurs.keySet());
				Collections.sort(sortedKey);
				for (String dateStr : sortedKey) {
					result.addRate(asset, LocalDate.parse(dateStr), kurs.get(dateStr));
				}
			}
		} catch (Exception e) {
			throw new CustomRequestException(e.getMessage());
		}

		return result;
	}

}
