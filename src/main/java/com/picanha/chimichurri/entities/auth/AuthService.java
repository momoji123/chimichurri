package com.picanha.chimichurri.entities.auth;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.picanha.chimichurri.entities.BaseServices;

import jakarta.persistence.EntityManager;

@Service
public class AuthService extends BaseServices<Auth>{
	public AuthService() {
		super(Auth.class);
	}

	@Autowired
	private PlatformTransactionManager txManager;
	@Autowired
	private EntityManager em;
	
	public Auth save(Auth obj) {
		return save(obj, txManager, em);
	}
	
	public Auth getById(int id) {		
		return getById(id, em);
	}

	@Override
	public List<Auth> getAll() {
		return getAll(em);
	}

	@Override
	public void deleteById(int id) {
		deleteById(id, txManager, em);
	}

	@Override
	public void delete(Auth obj) {
		delete(obj, txManager, em);
	}
}
