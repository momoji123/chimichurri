package com.picanha.chimichurri.entities.auth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.picanha.chimichurri.entities.BaseServices;

import jakarta.persistence.EntityManager;

@Service
public class AuthService extends BaseServices<Auth> {
	public AuthService(PlatformTransactionManager txManager, EntityManager em) {
		super(Auth.class, txManager, em);
	}
}
