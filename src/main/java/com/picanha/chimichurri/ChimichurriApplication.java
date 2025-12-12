package com.picanha.chimichurri;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.picanha.chimichurri.entities.factories.UserFactory;
import com.picanha.chimichurri.entities.user.User;
import com.picanha.chimichurri.entities.user.UserService;

@SpringBootApplication
public class ChimichurriApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChimichurriApplication.class, args);
	}

}
