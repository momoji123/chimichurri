package com.picanha.chimichurri.entities.factories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.picanha.chimichurri.entities.account.Account;
import com.picanha.chimichurri.entities.account.AccountService;
import com.picanha.chimichurri.entities.account.AccountType;
import com.picanha.chimichurri.entities.user.User;

@Service
public class AccountFactory {
	
	@Autowired
	private AccountService accountService;

	public List<Account> createNewAccount(User u) {
		List<Account> accList = new ArrayList<>();
		Account accCrypt = new Account();
		accCrypt.setType(AccountType.CRPYTO_WALLET);
		accCrypt.setUser(u);
		accList.add(accountService.saveAccount(accCrypt));
		
		Account accFiat = new Account();
		accFiat.setType(AccountType.FIAT_WALLET);
		accFiat.setUser(u);
		accList.add(accountService.saveAccount(accFiat));
		
		return accList;
	}
}
