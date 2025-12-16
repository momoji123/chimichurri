package com.picanha.chimichurri.entities.factories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.picanha.chimichurri.entities.auth.Auth;
import com.picanha.chimichurri.entities.user.User;
import com.picanha.chimichurri.entities.user.UserService;

@Service
public class UserFactory {
	
	@Autowired
	private UserService userService;
	@Autowired
	private AccountFactory accFactory;

	public User createNewUser(String name, String username, String password) {
		User u = new User();
		u.setName(name);
		
		//create auth
		Auth auth = new Auth();
		u.setAuth(auth);
		auth.setUsername(username);
		auth.setPassword(password);
		auth.setUser(u);
		
		u.setAuth(auth);
		
		userService.save(u);
		
		//create account
		accFactory.createNewAccount(u);
		
		return u;
	}
	
}
