package com.faw.poc.iotclientmock;

import com.faw.poc.iotclientmock.DirectlyConnectedMock.*;
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
	@Resource(name="MockHumidityService")
	private MockHumidityService mockHumidityService;
	@Resource(name="MockPressureService")
	private MockPressureService mockPressureService;
	@Resource(name="MockRefrigeratorService")
	private MockRefrigeratorService mockRefrigeratorService;
	@Resource(name="MockTemperatureService")
	private MockTemperatureService mockTemperatureService;
	@Resource(name="MockWaterPumpService")
	private MockWaterPumpService mockWaterPumpService;
	@Resource(name="MockClientService")
	private MockClientService mockClientService;

	public static void main(String[] args) {
		SpringApplication.run(IotclientmockApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		if(args.length!=3)return;
		switch (Integer.valueOf(args[2]).intValue()) {
			case 0:
				mockHumidityService.startWork(args[0], args[1]);
				break;
			case 1:
				mockPressureService.startWork(args[0], args[1]);
				break;
			case 2:
				mockRefrigeratorService.startWork(args[0], args[1]);
				break;
			case 3:
				mockTemperatureService.startWork(args[0], args[1]);
				break;
			case 4:
				mockWaterPumpService.startWork(args[0], args[1]);
				break;
			default:
				mockClientService.startWork(args[0],args[1]);
		}
	}
}
