package com.picanha.chimichurri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.picanha.chimichurri.entities")
public class ChimichurriApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChimichurriApplication.class, args);
	}

}
