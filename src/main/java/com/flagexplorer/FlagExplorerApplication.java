package com.flagexplorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FlagExplorerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlagExplorerApplication.class, args);
	}

}
