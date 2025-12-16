package com.picanha.chimichurri.entities.account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.picanha.chimichurri.entities.BaseServices;

import jakarta.persistence.EntityManager;

@Service
public class AccountService extends BaseServices<Account>{
	
	

	public AccountService() {
		super(Account.class);
	}

	@Autowired
	private PlatformTransactionManager txManager;
	@Autowired
	private EntityManager em;

	public Account save(Account account) {
		return save(account, txManager, em);
	}
	
	public Account getById(int id) {		
		return getById(id, em);
	}

	@Override
	public List<Account> getAll() {
		return getAll(em);
	}

	@Override
	public void deleteById(int id) {
		deleteById(id, txManager, em);
	}

	@Override
	public void delete(Account obj) {
		delete(obj, txManager, em);
	}
}
