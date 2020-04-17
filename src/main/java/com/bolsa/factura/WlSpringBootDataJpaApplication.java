package com.bolsa.factura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bolsa.factura.app.models.service.IUploadFileService;

@SpringBootApplication
public class WlSpringBootDataJpaApplication implements CommandLineRunner {

	@Autowired
	IUploadFileService uploadFileService;

	public static void main(String[] args) {
		SpringApplication.run(WlSpringBootDataJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		uploadFileService.deleteAll();
		uploadFileService.init();
		/*
		String password = "1234";
		
		
		for (int i = 0; i < 2; i++) {
			PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
			String encodedPass= encoder.encode(password);
			
			System.out.println("clave: " +encodedPass);
	
		}
		
		*/
	}

}
