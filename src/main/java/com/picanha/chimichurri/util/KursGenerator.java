package com.picanha.chimichurri.util;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;

public class KursGenerator {

	public static String getFilename(String currency, int month, int year) {
		String fileName = String.format("rates" + File.separator + "rates_%s_%02d_%d.json", currency, month, year);
		return fileName;
	}

	public static void generateYearlyRates(String currency, int year, double pivotRate, double yearlySwing,
			double monthlySwing) {
		for (int i = 1; i <= 12; i++) {
			String fileName = KursGenerator.getFilename(currency, i, year);
			boolean isExist = Files.isReadable(Paths.get(fileName));
			if (isExist) {
				continue;
			}

			KursGenerator.generateMonthlyRates(currency, year, i, pivotRate, monthlySwing);
			System.out.println("Rate form month: " + i + " was created");

			Random random = new Random();
			boolean isUp = random.nextBoolean();

			if (isUp) {
				pivotRate = pivotRate + (pivotRate * (yearlySwing / 100));
			} else {
				pivotRate = pivotRate - (pivotRate * (yearlySwing / 100));
			}
		}
	}

	private static void generateMonthlyRates(String currency, int year, int month, double pivotRate,
			double swingPercentage) {

		YearMonth ym = YearMonth.of(year, month);
		int daysInMonth = ym.lengthOfMonth();

		String fileName = getFilename(currency, month, year);

		// Calculate min and max based on pivot and swing
		double swingAmount = pivotRate * (swingPercentage / 100.0);
		double minRate = pivotRate - swingAmount;
		double maxRate = pivotRate + swingAmount;

		Map<String, BigDecimal> dailyRates = new LinkedHashMap<>();

		for (int day = 1; day <= daysInMonth; day++) {
			LocalDate date = LocalDate.of(year, month, day);

			double rate = getRandomDouble(minRate, maxRate);

			dailyRates.put(date.toString(), BigDecimal.valueOf(rate));
		}

		// Wrap in outer structure
		Map<String, Map<String, BigDecimal>> root = new LinkedHashMap<>();
		root.put(currency, dailyRates);
		RateFile rateFile = new RateFile(root);

		// Write file
		ObjectMapper mapper = new ObjectMapper();
		mapper.isEnabled(SerializationFeature.INDENT_OUTPUT);
		mapper.writeValue(new File(fileName), rateFile);

		System.out.println("File created: " + fileName);
	}

	private static double getRandomDouble(double minNumber, double maxNumber) {
		Random random = new Random();
		// Random value between minRate and maxRate
		double rate = minNumber + (random.nextDouble() * (maxNumber - minNumber));
		return rate;
	}

	public static BigDecimal getKurs(String currency, LocalDate date) {
		if (currency.equalsIgnoreCase("EUR")) {
			return BigDecimal.ONE;
		}
		Map<String, Map<String, BigDecimal>> rateData = readMonthlyRates(currency, date.getMonthValue(),
				date.getYear());
		BigDecimal rate = rateData.get(currency).get(date.toString());
		return rate;
	}

	public static Map<String, BigDecimal> getKurs(String currency, LocalDate fromDate, LocalDate toDate) {
		LocalDate prevDate = null;
		Map<String, Map<String, BigDecimal>> rateData = null;

		Map<String, BigDecimal> rateMap = new HashMap<>();
		for (LocalDate currDate = fromDate; fromDate.compareTo(toDate) <= 0; fromDate.plusDays(1)) {

			if (currency.equalsIgnoreCase("EUR")) {
				rateMap.put(currDate.toString(), BigDecimal.ONE);
				continue;
			}

			if (prevDate == null || currDate.getYear() > prevDate.getYear()) {
				rateData = readMonthlyRates(currency, currDate.getMonthValue(), currDate.getYear());
			}

			rateMap.put(currDate.toString(), rateData.get(currency).get(currDate.toString()));
			prevDate = currDate;
		}

		return rateMap;
	}

	private static Map<String, Map<String, BigDecimal>> readMonthlyRates(String currency, int month, int year) {
		String fileName = getFilename(currency, month, year);

		boolean isExist = Files.isReadable(Paths.get(fileName));
		if (!isExist) {
			double randRate = getRandomDouble(100, 100000);
			double swingRateMon = getRandomDouble(10, 30);
			double randRateYear = getRandomDouble(10, 40);
			KursGenerator.generateYearlyRates(currency, year, randRate, randRateYear, swingRateMon);
		}

		ObjectMapper mapper = new ObjectMapper();

		RateFile rateFile = mapper.readValue(new File(fileName), RateFile.class);

		return rateFile.getData();
	}

}
