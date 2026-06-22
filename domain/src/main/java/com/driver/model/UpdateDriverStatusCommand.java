package com.driver.model;

import com.driver.enums.DriverStatus;

public record UpdateDriverStatusCommand(
    String driverId,
    DriverStatus newStatus,
    long timestamp
) {}
