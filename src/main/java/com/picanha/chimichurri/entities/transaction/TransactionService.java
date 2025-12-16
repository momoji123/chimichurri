package com.picanha.chimichurri.entities.transaction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.picanha.chimichurri.entities.BaseServices;

import jakarta.persistence.EntityManager;

@Service
public class TransactionService extends BaseServices<Transaction>{
	public TransactionService() {
		super(Transaction.class);
	}

	@Autowired
	private PlatformTransactionManager txManager;
	@Autowired
	private EntityManager em;
	
	public Transaction save(Transaction obj) {
		return save(obj, txManager, em);
	}
	
	public Transaction getById(int id) {		
		return getById(id, em);
	}
	
	public List<Transaction> getAll(){
		return getAll();
	}

	@Override
	public void deleteById(int id) {
		deleteById(id, txManager, em);
	}

	@Override
	public void delete(Transaction obj) {
		delete(obj, txManager, em);
	}
}
