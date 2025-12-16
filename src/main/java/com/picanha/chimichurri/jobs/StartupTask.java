package com.picanha.chimichurri.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.picanha.chimichurri.entities.factories.UserFactory;
import com.picanha.chimichurri.entities.user.User;

import tools.jackson.databind.ObjectMapper;

@Component
public class StartupTask implements ApplicationRunner {

    @Autowired
	private UserFactory ufact;

    @Override
    public void run(ApplicationArguments args) {
    	initUsers();
    }

	private void initUsers() {
		System.out.println("Init users");
    	User u1 = ufact.createNewUser("test user 1", "testuser1", "testuser1");
		User u2 = ufact.createNewUser("test user 2", "testuser2", "testuser2");
		User u3 = ufact.createNewUser("test user 3", "testuser3", "testuser3");
		

		ObjectMapper om = new ObjectMapper();
		System.out.println(om.writeValueAsString(u1) + om.writeValueAsString(u1.getAuth()));
		System.out.println(om.writeValueAsString(u2) + om.writeValueAsString(u2.getAuth()));
		System.out.println(om.writeValueAsString(u3) + om.writeValueAsString(u3.getAuth()));
	}
}
