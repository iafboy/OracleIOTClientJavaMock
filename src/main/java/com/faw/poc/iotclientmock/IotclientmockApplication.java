package com.faw.poc.iotclientmock;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class IotclientmockApplication implements CommandLineRunner {
	@Resource(name="MockClientService")
	private MockClientService mockClientService;

	public static void main(String[] args) {
		SpringApplication.run(IotclientmockApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		mockClientService.startWork(args[0],args[1]);
	}
}
