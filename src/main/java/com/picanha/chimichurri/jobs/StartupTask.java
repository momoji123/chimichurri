package com.picanha.chimichurri.jobs;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.picanha.chimichurri.entities.factories.TransactionFactory;
import com.picanha.chimichurri.entities.factories.UserFactory;
import com.picanha.chimichurri.entities.user.User;
import com.picanha.chimichurri.entities.user.UserService;
import com.picanha.chimichurri.util.DummyHttpServletRequest;
import com.picanha.chimichurri.util.KursGenerator;

import jakarta.servlet.http.HttpServletRequest;
import tools.jackson.databind.ObjectMapper;

@Component
public class StartupTask implements ApplicationRunner {

	@Autowired
	private UserFactory ufact;
	@Autowired
	private TransactionFactory transFact;
	@Autowired
	private UserService userService; // just for commit. Can be any service

	@Override
	public void run(ApplicationArguments args) {
		HttpServletRequest req = new DummyHttpServletRequest();
		initKurs();
		List<User> userList = initUsers(req);
		initTransactions(userList.get(0), req);
		userService.commit(req);
		userService.rollback(req);
	}

	private List<User> initUsers(HttpServletRequest req) {
		List<User> userList = new ArrayList<>();
		System.out.println("Init users");
		User u1 = ufact.createNewUser("test user 1", "testuser1", "testuser1", req);
		User u2 = ufact.createNewUser("test user 2", "testuser2", "testuser2", req);
		User u3 = ufact.createNewUser("test user 3", "testuser3", "testuser3", req);

		ObjectMapper om = new ObjectMapper();
		System.out.println(om.writeValueAsString(u1) + om.writeValueAsString(u1.getAuth()));
		System.out.println(om.writeValueAsString(u2) + om.writeValueAsString(u2.getAuth()));
		System.out.println(om.writeValueAsString(u3) + om.writeValueAsString(u3.getAuth()));

		userList.add(u1);
		userList.add(u2);
		userList.add(u3);
		return userList;
	}

	private void initKurs() {
		double pivot_price = 70000.00;
		KursGenerator.generateYearlyRates("BTC", 2025, pivot_price, 18.00, 25.00);
	}

	private void initTransactions(User u, HttpServletRequest req) {
		ZonedDateTime transactionDate = ZonedDateTime.now();
		transactionDate = transactionDate.withMonth(3).withYear(2025);
		transFact.addFiatMoney(u, transactionDate, new BigDecimal("1500.00"), "EUR", req);

		Random rand = new Random();
		rand.setSeed(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

		transactionDate = ZonedDateTime.now();
		transactionDate = transactionDate.withDayOfMonth(15).withMonth(4).withYear(2025);
		transFact.createNewCryptoTransactionWithFiatAmount(u, transactionDate, new BigDecimal("430"),
				KursGenerator.getKurs("BTC", transactionDate.toLocalDate()), "EUR", "BTC", req);

		transactionDate = ZonedDateTime.now();
		transactionDate = transactionDate.withDayOfMonth(17).withMonth(6).withYear(2025);
		transFact.createNewCryptoTransactionWithFiatAmount(u, transactionDate, new BigDecimal("30"),
				KursGenerator.getKurs("BTC", transactionDate.toLocalDate()), "EUR", "BTC", req);

		transactionDate = ZonedDateTime.now();
		transactionDate = transactionDate.withDayOfMonth(15).withMonth(8).withYear(2025);
		transFact.createNewCryptoTransactionWithFiatAmount(u, transactionDate, new BigDecimal("250"),
				KursGenerator.getKurs("BTC", transactionDate.toLocalDate()), "EUR", "BTC", req);
	}
}
