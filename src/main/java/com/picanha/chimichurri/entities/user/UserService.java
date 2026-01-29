package com.picanha.chimichurri.entities.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.picanha.chimichurri.entities.BaseServices;
import com.picanha.chimichurri.entities.User;

import jakarta.persistence.EntityManager;

@Service
public class UserService extends BaseServices<User> {
	public UserService(PlatformTransactionManager txManager, EntityManager em) {
		super(User.class, txManager, em);
	}
}
