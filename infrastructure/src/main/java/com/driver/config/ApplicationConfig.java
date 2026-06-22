package com.driver.config;

import com.driver.port.out.DriverProducer;
import com.driver.service.UpdateDriverLocationService;
import com.driver.service.UpdateDriverStatusService;
import com.driver.port.in.UpdateDriverLocationUseCase;
import com.driver.port.in.UpdateDriverStatusUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public UpdateDriverLocationUseCase updateDriverLocationUseCase(DriverProducer driverProducer) {
        return new UpdateDriverLocationService(driverProducer);
    }

    @Bean
    public UpdateDriverStatusUseCase updateDriverStatusUseCase(DriverProducer driverProducer) {
        return new UpdateDriverStatusService(driverProducer);
    }
}
