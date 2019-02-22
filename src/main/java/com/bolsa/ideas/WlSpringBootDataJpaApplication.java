package com.bolsa.ideas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bolsa.ideas.app.models.service.IUploadFileService;

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

	}

}
