package com.driver.service;

import com.driver.port.out.DriverProducer;
import com.driver.model.DriverLocationEvent;
import com.driver.model.UpdateDriverLocationCommand;
import com.driver.port.in.UpdateDriverLocationUseCase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateDriverLocationService implements UpdateDriverLocationUseCase {

	private final DriverProducer driverProducer;

	@Override
	public void execute(UpdateDriverLocationCommand command) {
		final DriverLocationEvent event = DriverLocationEvent.builder().driverId(command.driverId())
				.latitude(command.location().latitude()).longitude(command.location().longitude())
				.timestamp(command.timestamp()).build();

		this.driverProducer.sendDriverLocation(event);
	}
}
