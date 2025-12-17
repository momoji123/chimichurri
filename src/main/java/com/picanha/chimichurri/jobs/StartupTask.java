package com.picanha.chimichurri.jobs;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.picanha.chimichurri.entities.factories.TransactionFactory;
import com.picanha.chimichurri.entities.factories.UserFactory;
import com.picanha.chimichurri.entities.transaction.TransactionService;
import com.picanha.chimichurri.entities.user.User;

import tools.jackson.databind.ObjectMapper;

@Component
public class StartupTask implements ApplicationRunner {

    @Autowired
	private UserFactory ufact;
    @Autowired
    private TransactionFactory transFact;

    @Override
    public void run(ApplicationArguments args) {
    	List<User> userList = initUsers();
    	initTransactions(userList.get(0));
    }

	private List<User> initUsers() {
		List<User> userList = new ArrayList<>();
		System.out.println("Init users");
    	User u1 = ufact.createNewUser("test user 1", "testuser1", "testuser1");
		User u2 = ufact.createNewUser("test user 2", "testuser2", "testuser2");
		User u3 = ufact.createNewUser("test user 3", "testuser3", "testuser3");
		

		ObjectMapper om = new ObjectMapper();
		System.out.println(om.writeValueAsString(u1) + om.writeValueAsString(u1.getAuth()));
		System.out.println(om.writeValueAsString(u2) + om.writeValueAsString(u2.getAuth()));
		System.out.println(om.writeValueAsString(u3) + om.writeValueAsString(u3.getAuth()));
		
		userList.add(u1);
		userList.add(u2);
		userList.add(u3);
		return userList;
	}
	
	private void initTransactions(User u) {
		ZonedDateTime transactionDate = ZonedDateTime.now();
		transactionDate.withMonth(3);
		transactionDate.withYear(2025);
		transFact.addFiatMoney(u, transactionDate, new BigDecimal("1500.00"), "EUR");
		
		transactionDate = ZonedDateTime.now();
		transactionDate.withDayOfMonth(15);
		transactionDate.withMonth(4);
		transactionDate.withYear(2025);
		transFact.createNewCryptoTransaction(u, transactionDate, new BigDecimal("0.01"), new BigDecimal("15360.00"), "EUR", "BTC");
	}
}
