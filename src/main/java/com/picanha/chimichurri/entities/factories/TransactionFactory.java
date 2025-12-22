package com.picanha.chimichurri.entities.factories;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.picanha.chimichurri.entities.account.Account;
import com.picanha.chimichurri.entities.account.AccountService;
import com.picanha.chimichurri.entities.account.AccountType;
import com.picanha.chimichurri.entities.transaction.Transaction;
import com.picanha.chimichurri.entities.transaction.TransactionService;
import com.picanha.chimichurri.entities.user.User;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class TransactionFactory {

	@Autowired
	private TransactionService transService;
	@Autowired
	private AccountService accService;

	public List<Transaction> createNewCryptoTransaction(User u, ZonedDateTime date, BigDecimal amount, BigDecimal rate,
			String fiatAsset, String cryptoAsset, HttpServletRequest req) {
		List<Account> accountList = accService.getByUser(u);
		List<Transaction> transList = new ArrayList<>();
		String baseTID = "tx-" + u.getId() + "-"
				+ ZonedDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmssSSS"));
		// incoming To Crypto Account
		Transaction t = new Transaction();
		t.setAccount(accountList.stream().filter(acc -> acc.getType().equals(AccountType.CRYPTO_WALLET))
				.collect(Collectors.toList()).get(0));
		t.setAmount(amount);
		t.setAsset(cryptoAsset);
		t.setBaseAsset(fiatAsset);
		t.setRate(rate);
		t.setTransactionDate(date);
		t.setTransactionId(baseTID + "-in");

		// outcomming
		Transaction tOut = new Transaction();
		tOut.setAccount(accountList.stream().filter(acc -> acc.getType().equals(AccountType.FIAT_WALLET))
				.collect(Collectors.toList()).get(0));
		tOut.setAmount(amount.multiply(rate).negate());
		tOut.setAsset(fiatAsset);
		tOut.setBaseAsset(fiatAsset);
		tOut.setRate(BigDecimal.ONE);
		tOut.setTransactionDate(date);
		tOut.setTransactionId(baseTID + "-out");

		t = transService.save(t, req);
		transList.add(t);
		tOut = transService.save(tOut, req);
		transList.add(tOut);
		return transList;
	}

	public List<Transaction> createNewCryptoTransactionWithFiatAmount(User u, ZonedDateTime date, BigDecimal fiatAmount,
			BigDecimal rate, String fiatAsset, String cryptoAsset, HttpServletRequest req) {
		List<Account> accountList = accService.getByUser(u);
		List<Transaction> transList = new ArrayList<>();
		String baseTID = "tx-" + u.getId() + "-"
				+ ZonedDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmssSSS"));
		// incoming To Crypto Account
		Transaction t = new Transaction();
		t.setAccount(accountList.stream().filter(acc -> acc.getType().equals(AccountType.CRYPTO_WALLET))
				.collect(Collectors.toList()).get(0));
		t.setAmount(fiatAmount.setScale(12).divide(rate, RoundingMode.HALF_UP));
		t.setAsset(cryptoAsset);
		t.setBaseAsset(fiatAsset);
		t.setRate(rate);
		t.setTransactionDate(date);
		t.setTransactionId(baseTID + "-in");

		// outcomming
		Transaction tOut = new Transaction();
		tOut.setAccount(accountList.stream().filter(acc -> acc.getType().equals(AccountType.FIAT_WALLET))
				.collect(Collectors.toList()).get(0));
		tOut.setAmount(fiatAmount.negate());
		tOut.setAsset(fiatAsset);
		tOut.setBaseAsset(fiatAsset);
		tOut.setRate(BigDecimal.ONE);
		tOut.setTransactionDate(date);
		tOut.setTransactionId(baseTID + "-out");

		t = transService.save(t, req);
		transList.add(t);
		tOut = transService.save(tOut, req);
		transList.add(tOut);
		return transList;
	}

	public Transaction addFiatMoney(User u, ZonedDateTime date, BigDecimal amount, String asset,
			HttpServletRequest req) {
		List<Account> accountList = accService.getByUser(u);
		String baseTID = "tx-" + u.getId() + "-"
				+ ZonedDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmssSSS"));
		// add fiat money
		Transaction t = new Transaction();
		t.setAccount(accountList.stream().filter(acc -> acc.getType().equals(AccountType.FIAT_WALLET))
				.collect(Collectors.toList()).get(0));
		t.setAmount(amount);
		t.setAsset(asset);
		t.setBaseAsset(asset);
		t.setRate(BigDecimal.ONE);
		t.setTransactionDate(date);
		t.setTransactionId(baseTID + "-in");

		t = transService.save(t, req);

		return t;
	}

	public void commit(HttpServletRequest req) {
		accService.commit(req);
	}

	public void rollback(HttpServletRequest req) {
		accService.rollback(req);
	}
}
