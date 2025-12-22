package com.picanha.chimichurri.api.pub.controller;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.picanha.chimichurri.api.pub.dto.TransactionDTO;
import com.picanha.chimichurri.entities.account.AccountType;
import com.picanha.chimichurri.entities.transaction.Transaction;
import com.picanha.chimichurri.entities.transaction.TransactionService;
import com.picanha.chimichurri.entities.user.User;
import com.picanha.chimichurri.entities.user.UserService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE }, allowedHeaders = { "*" })
public class TransactionController {

	@Autowired
	private TransactionService ts;
	@Autowired
	private UserService us;

	@GetMapping(path = "/transactions")
	private List<TransactionDTO> getTransactions(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime fromDate, // 2007-12-03T10:15:30+01:00
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime toDate, // 2007-12-03T10:15:30+01:00
			@RequestParam AccountType type, @RequestParam(required = false) String asset) {
		// for now always use test user 1
		User u = us.getById(1);

		List<Transaction> transList = ts.getBy(u, type, asset, fromDate, toDate);
		List<TransactionDTO> resultList = new ArrayList<>();

		for (Transaction t : transList) {
			resultList.add(new TransactionDTO(t));
		}

		return resultList;
	}
}
