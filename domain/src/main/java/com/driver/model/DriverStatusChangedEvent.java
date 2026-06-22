package com.driver.model;

import com.driver.enums.DriverStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DriverStatusChangedEvent {
    private String driverId;
    private DriverStatus previousStatus;
    private DriverStatus newStatus;
    private long timestamp;
}
