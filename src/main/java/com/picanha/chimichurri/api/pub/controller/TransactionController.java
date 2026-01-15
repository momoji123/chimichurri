package com.picanha.chimichurri.api.pub.controller;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.picanha.chimichurri.CryptoConfigService;
import com.picanha.chimichurri.api.pub.controller.configs.CustomRequestException;
import com.picanha.chimichurri.api.pub.dto.BaseTransactionDTO;
import com.picanha.chimichurri.api.pub.dto.TransactionDTO;
import com.picanha.chimichurri.api.pub.dto.TransactionType;
import com.picanha.chimichurri.api.pub.dto.TransactionWithSummaryDTO;
import com.picanha.chimichurri.entities.account.AccountType;
import com.picanha.chimichurri.entities.factories.TransactionFactory;
import com.picanha.chimichurri.entities.transaction.Transaction;
import com.picanha.chimichurri.entities.transaction.TransactionService;
import com.picanha.chimichurri.entities.user.User;
import com.picanha.chimichurri.entities.user.UserService;
import com.picanha.chimichurri.util.KursGenerator;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE }, allowedHeaders = { "*" })
public class TransactionController {

	@Autowired
	private TransactionService ts;
	@Autowired
	private UserService us;
	@Autowired
	private TransactionFactory tf;
	@Autowired
	private CryptoConfigService cryptoConfig;

	@Operation(summary = "Get transactions summary", description = "Return Transaction summary and transactions (optional). Client might also filter by asset.")
	@GetMapping(path = "/summary")
	private TransactionWithSummaryDTO getTransactionsSummary(@RequestParam AccountType type,
			@RequestParam(required = false) String asset,
			@RequestParam(required = false, defaultValue = "false") boolean showTransactions,
			HttpServletRequest request) {
		// for now always use test user 1
		User u = us.getById(1);

		List<TransactionDTO> dtoList = new ArrayList<>();
		for (Transaction t : ts.getBy(u, type, asset, null, null)) {
			dtoList.add(new TransactionDTO(t));
		}

		TransactionWithSummaryDTO result = new TransactionWithSummaryDTO(dtoList, true);
		if (!showTransactions) {
			result.setTransactions(new ArrayList<>());
		}
		return result;
	}

	@Operation(summary = "Get transactions (with summary)", description = "Return Transaction transactions and summary of it. Client might also filter by asset, fromDate and toDate. Date must be in ISO.DATE_TIME Format (Ex: '2007-12-15T10:15:30+01:00')")
	@GetMapping()
	private TransactionWithSummaryDTO getTransactions(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime fromDate, // 2007-12-15T10:15:30+01:00
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime toDate, // 2007-12-15T10:15:30+01:00
			@RequestParam AccountType type, @RequestParam(required = false) String asset, HttpServletRequest request) {
		// for now always use test user 1
		User u = us.getById(1);

		List<TransactionDTO> dtoList = new ArrayList<>();
		for (Transaction t : ts.getBy(u, type, asset, fromDate, toDate)) {
			dtoList.add(new TransactionDTO(t));
		}

		TransactionWithSummaryDTO result = new TransactionWithSummaryDTO(dtoList, true);

		return result;
	}

	@Operation(summary = "Add Fiat money to wallet", description = "Add fiat money to wallet. Client must fill asset, type (sell/buy) and amount in body. ")
	@PutMapping(path = "/fiat-money")
	private TransactionDTO saveTransaction(@RequestBody(required = true) BaseTransactionDTO dto,
			HttpServletRequest request) {

		// for now always use test user 1
		User u = us.getById(1);

		if (dto.getAsset() == null || dto.getAsset().equals("EUR")) {
			// for now accepted only EUR!
			dto.setAsset("EUR");

			BigDecimal amount = dto.getAmount();

			if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
				throw new CustomRequestException("property 'amount' must be > 0");
			}

			if (dto.getType() == null) {
				throw new CustomRequestException("property 'type' must be buy or sell");
			} else if (dto.getType().equals(TransactionType.sell)) {
				amount = amount.negate();
			}

			Transaction t = tf.addFiatMoney(u, ZonedDateTime.now(), amount, dto.getAsset(), request);

			checkFiatAmount(request);

			tf.commit(request);
			TransactionDTO result = new TransactionDTO(t);
			return result;
		} else {
			tf.rollback(request);
			throw new CustomRequestException("Accepted Asset for this api is EUR");
		}
	}

	public void checkFiatAmount(HttpServletRequest request) {
		TransactionWithSummaryDTO transactionsSummary = getTransactionsSummary(AccountType.FIAT_WALLET, null, false,
				request);
		BigDecimal currentValue = transactionsSummary.getSummary().get("EUR").getCurrentValue();

		if (currentValue.compareTo(BigDecimal.ZERO) < 0) {
			tf.rollback(request);
			throw new CustomRequestException("Portofolio amount is not sufficient for this transaction");
		}
	}

	public void checkCryptoAmount(String asset, HttpServletRequest request) {
		TransactionWithSummaryDTO transactionsSummary = getTransactionsSummary(AccountType.CRYPTO_WALLET, null, false,
				request);
		BigDecimal currentValue = transactionsSummary.getSummary().get(asset).getCurrentValue();

		if (currentValue.compareTo(BigDecimal.ZERO) < 0) {
			tf.rollback(request);
			throw new CustomRequestException("Portofolio amount is not sufficient for this transaction");
		}
	}

	@Operation(summary = "Buy crypto", description = "This API is for client to buy crypto assets. Client must define asset, type (sell/buy) and amount (EUR) in body.")
	@PutMapping(path = "/crypto")
	private List<TransactionDTO> saveCryptoTransaction(@RequestBody(required = true) BaseTransactionDTO dto,
			HttpServletRequest request) {

		// for now always use test user 1
		User u = us.getById(1);

		if (cryptoConfig.isCryptoAssetAcceptable(dto.getAsset())) {

			BigDecimal amount = dto.getAmount();

			if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
				throw new CustomRequestException("property 'amount' must be > 0");
			}

			if (dto.getType() == null) {
				throw new CustomRequestException("property 'type' must be buy or sell");
			} else if (dto.getType().equals(TransactionType.sell)) {
				amount = amount.negate();
			}

			ZonedDateTime now = ZonedDateTime.now();
			List<Transaction> tList = tf.createNewCryptoTransactionWithFiatAmount(u, now, amount,
					KursGenerator.getKurs(dto.getAsset(), now.toLocalDate()), "EUR", dto.getAsset(), request);

			checkFiatAmount(request);
			checkCryptoAmount(dto.getAsset(), request);

			tf.commit(request);

			List<TransactionDTO> result = new ArrayList<>();
			for (Transaction currT : tList) {
				result.add(new TransactionDTO(currT));
			}

			return result;
		} else {
			tf.rollback(request);
			throw new CustomRequestException(
					String.format("Crypto %s asset is currently not available", dto.getAsset()));
		}
	}
}
