package org.wongws.hichat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class HichatApplication {

	public static void main(String[] args) {
		SpringApplication.run(HichatApplication.class, args);
	}

}
