package com.dna.backend.DNABackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class DnaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DnaBackendApplication.class, args);
	}

}
