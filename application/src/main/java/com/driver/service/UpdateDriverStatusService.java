package com.driver.service;

import com.driver.enums.DriverStatus;
import com.driver.model.DriverStatusChangedEvent;
import com.driver.model.UpdateDriverStatusCommand;
import com.driver.port.in.UpdateDriverStatusUseCase;
import com.driver.port.out.DriverProducer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateDriverStatusService implements UpdateDriverStatusUseCase {

    private final DriverProducer driverProducer;

    @Override
    public void execute(UpdateDriverStatusCommand command) {
        // TODO: Aquí deberías obtener el estado anterior del driver desde una base de datos
        // Por ahora, lo dejamos como null pero deberías implementar la persistencia
        DriverStatus previousStatus = null; // Obtener desde repositorio
        
        final DriverStatusChangedEvent event = DriverStatusChangedEvent.builder()
                .driverId(command.driverId())
                .previousStatus(previousStatus)
                .newStatus(command.newStatus())
                .timestamp(command.timestamp())
                .build();

        this.driverProducer.sendDriverStatusChanged(event);
    }
}
