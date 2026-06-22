package com.driver.adapter.in.rest;

import com.driver.enums.DriverStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateDriverStatusRequest(
    @NotNull(message = "Status is required")
    DriverStatus status
) {}
