package com.ylc.progress_management_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.ylc.progress_management_system.repository")
@EntityScan(basePackages = "com.ylc.progress_management_system.entity")
public class ProgressManagementSystemApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(ProgressManagementSystemApplication.class, args);
	}
}
